package com.Famdiba1.Huqsoz.presentation.menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Famdiba1.Huqsoz.R
import com.Famdiba1.Huqsoz.core.di.AppViewModelFactory
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.Pistachio
import com.Famdiba1.Huqsoz.core.theme.Raspberry
import com.Famdiba1.Huqsoz.core.theme.RaspberryLight
import com.Famdiba1.Huqsoz.core.theme.SurfaceAlt
import com.Famdiba1.Huqsoz.core.theme.SurfaceLight
import com.Famdiba1.Huqsoz.core.theme.SurfaceSoft
import com.Famdiba1.Huqsoz.core.theme.TextPrimary
import com.Famdiba1.Huqsoz.core.theme.TextSecondary
import com.Famdiba1.Huqsoz.core.theme.VanillaCream
import com.Famdiba1.Huqsoz.core.ui.ClayCard
import com.Famdiba1.Huqsoz.core.ui.FlavorBadge
import com.Famdiba1.Huqsoz.core.ui.PrimaryButton
import com.Famdiba1.Huqsoz.core.ui.ScreenBackground
import com.Famdiba1.Huqsoz.core.ui.SecondaryButton
import com.Famdiba1.Huqsoz.core.ui.SectionTitle
import com.Famdiba1.Huqsoz.core.ui.StatCard
import com.Famdiba1.Huqsoz.core.ui.groupedNumber
import com.Famdiba1.Huqsoz.data.sample.SampleData

@Composable
fun MenuScreen(
    onPlay: (Int, Int) -> Unit,
    onLevels: () -> Unit,
    onHowToPlay: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    MenuContent(
        state = state,
        onPlay = { onPlay(state.resumeChapter, state.resumeBoard) },
        onLevels = onLevels,
        onHowToPlay = onHowToPlay,
        onSettings = onSettings
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onHowToPlay: () -> Unit,
    onSettings: () -> Unit
) {
    val pulse = rememberInfiniteTransition(label = "menuPulse")
    val tileScale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "playPulse"
    )

    ScreenBackground(
        overlay = listOf(
            SurfaceAlt.copy(alpha = 0.90f),
            SurfaceSoft.copy(alpha = 0.92f),
            SurfaceLight.copy(alpha = 0.88f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = stringResource(R.string.app_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(color = SurfaceLight.copy(alpha = 0.7f), shape = CircleShape)
                        .border(width = 1.dp, color = Copper.copy(alpha = 0.35f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_home),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            SectionTitle(
                text = stringResource(R.string.menu_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(212.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ClayCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .graphicsLayer {
                            scaleX = tileScale
                            scaleY = tileScale
                        },
                    containerColor = Raspberry,
                    borderColor = Copper.copy(alpha = 0.45f),
                    radius = 28.dp,
                    elevation = 16.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(listOf(Raspberry, RaspberryLight)),
                                shape = RoundedCornerShape(28.dp)
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_1),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = stringResource(
                                R.string.board_label,
                                state.resumeChapter + 1,
                                state.resumeBoard + 1
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = VanillaCream,
                            textAlign = TextAlign.Center
                        )
                        PrimaryButton(
                            text = stringResource(R.string.play),
                            onClick = onPlay,
                            modifier = Modifier.fillMaxWidth(),
                            height = 58.dp,
                            startColor = VanillaCream,
                            endColor = SurfaceAlt,
                            labelColor = Raspberry
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (state.hasProgress) {
                        StatCard(
                            value = groupedNumber(state.bestScore),
                            label = stringResource(R.string.best_score),
                            valueColor = Copper,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                        StatCard(
                            value = stringResource(R.string.chip_progress, state.stars, state.totalStars),
                            label = stringResource(R.string.stars_earned),
                            valueColor = Pistachio,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    } else {
                        ClayCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            containerColor = SurfaceLight.copy(alpha = 0.84f),
                            borderColor = Copper.copy(alpha = 0.3f),
                            radius = 24.dp
                        ) {
                            Text(
                                text = stringResource(R.string.empty_progress),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(horizontal = 12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.size(14.dp))

            SecondaryButton(
                text = stringResource(R.string.levels),
                onClick = onLevels,
                modifier = Modifier.fillMaxWidth(),
                height = 56.dp,
                accent = Copper
            )

            Text(
                text = stringResource(R.string.levels_sub),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(modifier = Modifier.size(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.how_to_play),
                    onClick = onHowToPlay,
                    modifier = Modifier.weight(1f),
                    height = 54.dp,
                    accent = Pistachio
                )
                SecondaryButton(
                    text = stringResource(R.string.settings),
                    onClick = onSettings,
                    modifier = Modifier.weight(1f),
                    height = 54.dp,
                    accent = Copper
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            ClayCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                containerColor = SurfaceLight.copy(alpha = 0.72f),
                borderColor = Copper.copy(alpha = 0.3f),
                radius = 24.dp,
                elevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (flavor in SampleData.flavorOrder) {
                        FlavorBadge(flavor = flavor, size = 34.dp, showMark = true)
                    }
                }
            }

            if (state.tokens > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_4),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(R.string.copper_tokens, state.tokens),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Copper
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
    }
}

