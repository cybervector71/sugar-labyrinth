package com.Famdiba1.Huqsoz.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Famdiba1.Huqsoz.core.config.GameConfig
import com.Famdiba1.Huqsoz.data.sample.SampleData
import com.Famdiba1.Huqsoz.domain.model.Flavor
import com.Famdiba1.Huqsoz.domain.model.RoundOutcome
import com.Famdiba1.Huqsoz.domain.model.RoundResult
import com.Famdiba1.Huqsoz.domain.model.TileKind
import com.Famdiba1.Huqsoz.domain.repository.ProgressRepository
import com.Famdiba1.Huqsoz.domain.repository.SettingsRepository
import com.Famdiba1.Huqsoz.domain.usecase.GenerateBoardUseCase
import com.Famdiba1.Huqsoz.domain.usecase.PourResult
import com.Famdiba1.Huqsoz.domain.usecase.RotateTileUseCase
import com.Famdiba1.Huqsoz.domain.usecase.RunPourUseCase
import com.Famdiba1.Huqsoz.domain.usecase.ScoreRoundUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val chapter: Int,
    private val boardIndex: Int,
    private val expertRequested: Boolean,
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val generateBoard: GenerateBoardUseCase,
    private val runPour: RunPourUseCase,
    private val scoreRound: ScoreRoundUseCase,
    private val rotateTile: RotateTileUseCase
) : ViewModel() {

    private val internal = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = internal.asStateFlow()

    private val channel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = channel.receiveAsFlow()

    private var pourJob: Job? = null
    private var ghostJob: Job? = null
    private var hintJob: Job? = null
    private var shakeJob: Job? = null
    private var attempt = 0

    init {
        startBoard()
    }

    private fun startBoard() {
        val settings = settingsRepository.settings.value
        val expert = expertRequested || settings.expertBoards
        val budget = if (expert) GameConfig.MOVES_BUDGET_EXPERT else GameConfig.MOVES_BUDGET
        val cap = if (expert) GameConfig.PRESSURE_MAX_EXPERT else GameConfig.PRESSURE_MAX
        val spec = SampleData.chapter(chapter)
        val generated = generateBoard(spec, boardIndex, expert, attempt, budget)
        attempt += 1
        internal.value = GameUiState(
            board = generated.board,
            plan = generated.plan,
            delivered = emptyMap(),
            phase = GamePhase.IDLE,
            chapter = chapter,
            boardIndex = boardIndex,
            expert = expert,
            moves = budget,
            movesBudget = budget,
            pressure = 0,
            pressureMax = cap,
            dryRunsLeft = GameConfig.DRY_RUNS_PER_BOARD,
            tokens = progressRepository.progress.value.tokens,
            flavorMarks = settings.flavorMarks,
            hintVisible = true,
            seed = generated.board.seed
        )
        hintJob?.cancel()
        hintJob = viewModelScope.launch {
            delay(GameConfig.HINT_MS)
            internal.update { it.copy(hintVisible = false) }
        }
        if (settings.autoDryRun) {
            dryRun()
        }
    }

    fun onTileTap(index: Int) {
        val current = internal.value
        if (current.busy) return
        val tile = current.board.tiles.getOrNull(index) ?: return
        if (!tile.interactive) {
            shake(index)
            return
        }
        val free = current.tokenArmed
        val wet = current.flow.containsKey(index)
        val pressureCost = when {
            free -> 0
            wet -> GameConfig.WET_PRESSURE
            else -> GameConfig.DRY_PRESSURE
        }
        val moveCost = if (free) 0 else GameConfig.ROTATE_COST
        val nextBoard = rotateTile(current.board, index)
        if (free) progressRepository.spendToken()
        val flipped = tile.kind == TileKind.SWITCH
        val updated = current.copy(
            board = nextBoard,
            moves = current.moves - moveCost,
            pressure = (current.pressure + pressureCost).coerceAtLeast(0),
            tokens = if (free) (current.tokens - 1).coerceAtLeast(0) else current.tokens,
            tokenArmed = false,
            bonusHeld = current.bonusHeld && !flipped,
            flow = emptyMap(),
            fillDistance = 0,
            ghost = false
        )
        internal.value = updated
        checkLimits(updated)
    }

    fun armToken() {
        val current = internal.value
        if (current.busy || current.tokens <= 0) return
        internal.update { it.copy(tokenArmed = !it.tokenArmed) }
    }

    fun vent() {
        val current = internal.value
        if (current.busy) return
        if (current.pressure < 3 || current.moves < GameConfig.VENT_MOVE_COST) return
        val updated = current.copy(
            pressure = (current.pressure - GameConfig.VENT_RELIEF).coerceAtLeast(0),
            moves = current.moves - GameConfig.VENT_MOVE_COST
        )
        internal.value = updated
        checkLimits(updated)
    }

    fun dryRun() {
        val current = internal.value
        if (current.phase == GamePhase.POURING || current.phase == GamePhase.FINISHED) return
        if (current.dryRunsLeft <= 0) return
        val preview = runPour(current.board)
        internal.update {
            it.copy(
                phase = GamePhase.DRY_RUN,
                dryRunsLeft = it.dryRunsLeft - 1,
                flow = preview.flow,
                fillDistance = preview.maxDistance,
                ghost = true
            )
        }
        ghostJob?.cancel()
        ghostJob = viewModelScope.launch {
            delay(GameConfig.DRY_RUN_MS)
            internal.update {
                if (it.phase == GamePhase.DRY_RUN) {
                    it.copy(phase = GamePhase.IDLE, flow = emptyMap(), fillDistance = 0, ghost = false)
                } else {
                    it
                }
            }
        }
    }

    fun pour() {
        val current = internal.value
        if (current.busy) return
        val result = runPour(current.board)
        internal.update {
            it.copy(phase = GamePhase.POURING, flow = result.flow, fillDistance = 0, ghost = false)
        }
        pourJob?.cancel()
        pourJob = viewModelScope.launch {
            for (step in 0..result.maxDistance) {
                internal.update { it.copy(fillDistance = step) }
                delay(GameConfig.POUR_STEP_MS)
            }
            resolve(result)
        }
    }

    private fun resolve(result: PourResult) {
        val current = internal.value
        val merged = HashMap<Int, Int>()
        val delivered = HashMap<Flavor, Int>()
        for (flavor in Flavor.entries) {
            val before = current.delivered[flavor] ?: 0
            val required = current.plan[flavor] ?: 0
            val gained = result.delivered[flavor] ?: 0
            val after = minOf(before + gained, maxOf(required, before))
            if (after > 0) delivered[flavor] = after
            merged[flavor.ordinal] = after - before
        }
        val gainedUnits = merged.values.sum()
        val spoiled = result.spoiledBy
        val next = current.copy(
            delivered = delivered,
            phase = GamePhase.IDLE,
            fillDistance = result.maxDistance
        )
        when {
            spoiled != null -> {
                internal.value = next
                finish(RoundOutcome.BATCH_SPOILED, next, spoiled)
            }
            next.planComplete -> {
                internal.value = next
                finish(RoundOutcome.PLAN_FILLED, next, null)
            }
            else -> {
                val penalty = if (gainedUnits <= 0) GameConfig.WASTED_POUR_PRESSURE else 0
                val updated = next.copy(pressure = next.pressure + penalty)
                internal.value = updated
                checkLimits(updated)
            }
        }
    }

    private fun checkLimits(current: GameUiState) {
        if (current.phase == GamePhase.FINISHED) return
        if (current.pressure >= current.pressureMax) {
            finish(RoundOutcome.LINE_BURST, current, null)
            return
        }
        if (current.moves <= 0 && !current.planComplete) {
            finish(RoundOutcome.OUT_OF_MOVES, current, null)
        }
    }

    private fun finish(outcome: RoundOutcome, snapshot: GameUiState, spoiledBy: Flavor?) {
        pourJob?.cancel()
        ghostJob?.cancel()
        hintJob?.cancel()
        internal.update { it.copy(phase = GamePhase.FINISHED, tokenArmed = false) }
        val score = scoreRound(
            outcome = outcome,
            delivered = snapshot.delivered,
            required = snapshot.plan,
            movesLeft = snapshot.moves,
            movesBudget = snapshot.movesBudget,
            bonusHeld = snapshot.bonusHeld
        )
        progressRepository.recordRound(
            chapter = snapshot.chapter,
            board = snapshot.boardIndex,
            stars = score.stars,
            score = score.score,
            tokensEarned = score.tokensEarned
        )
        val result = RoundResult(
            outcome = outcome,
            chapter = snapshot.chapter,
            board = snapshot.boardIndex,
            expert = snapshot.expert,
            delivered = snapshot.delivered,
            required = snapshot.plan,
            movesLeft = snapshot.moves.coerceAtLeast(0),
            stars = score.stars,
            score = score.score,
            tokensEarned = score.tokensEarned,
            bonusHeld = snapshot.bonusHeld,
            spoiledBy = spoiledBy,
            hasNextBoard = snapshot.boardIndex + 1 < GameConfig.BOARDS_PER_CHAPTER
        )
        viewModelScope.launch { channel.send(GameEvent.Finished(result)) }
    }

    private fun shake(index: Int) {
        shakeJob?.cancel()
        internal.update { it.copy(shakeCell = index) }
        shakeJob = viewModelScope.launch {
            delay(GameConfig.SHAKE_MS)
            internal.update { it.copy(shakeCell = -1) }
        }
    }

    fun openPause() {
        if (internal.value.phase == GamePhase.FINISHED) return
        pourJob?.cancel()
        internal.update { it.copy(pauseOpen = true, phase = GamePhase.IDLE) }
    }

    fun closePause() {
        internal.update { it.copy(pauseOpen = false) }
    }

    fun restart() {
        pourJob?.cancel()
        ghostJob?.cancel()
        startBoard()
    }

    fun requestExit() {
        if (settingsRepository.settings.value.confirmExit && internal.value.phase != GamePhase.FINISHED) {
            internal.update { it.copy(exitOpen = true, pauseOpen = false) }
        } else {
            leave()
        }
    }

    fun dismissExit() {
        internal.update { it.copy(exitOpen = false) }
    }

    fun leave() {
        pourJob?.cancel()
        ghostJob?.cancel()
        hintJob?.cancel()
        internal.update { it.copy(exitOpen = false, pauseOpen = false) }
        viewModelScope.launch { channel.send(GameEvent.ExitToMenu) }
    }

    fun pause() {
        pourJob?.cancel()
        ghostJob?.cancel()
        internal.update {
            if (it.phase == GamePhase.POURING || it.phase == GamePhase.DRY_RUN) {
                it.copy(phase = GamePhase.IDLE)
            } else {
                it
            }
        }
    }

    override fun onCleared() {
        pourJob?.cancel()
        ghostJob?.cancel()
        hintJob?.cancel()
        shakeJob?.cancel()
        super.onCleared()
    }
}
