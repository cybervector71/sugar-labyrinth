package com.Famdiba1.Huqsoz.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Famdiba1.Huqsoz.R
import com.Famdiba1.Huqsoz.core.di.AppViewModelFactory
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.DisabledFill
import com.Famdiba1.Huqsoz.core.theme.GlassTint
import com.Famdiba1.Huqsoz.core.theme.Pistachio
import com.Famdiba1.Huqsoz.core.theme.Raspberry
import com.Famdiba1.Huqsoz.core.theme.SurfaceLight
import com.Famdiba1.Huqsoz.core.theme.SurfaceSoft
import com.Famdiba1.Huqsoz.core.theme.TextPrimary
import com.Famdiba1.Huqsoz.core.theme.TextSecondary
import com.Famdiba1.Huqsoz.core.theme.VanillaCream
import com.Famdiba1.Huqsoz.core.ui.ClayCard
import com.Famdiba1.Huqsoz.core.ui.ScreenBackground
import com.Famdiba1.Huqsoz.core.ui.SecondaryButton
import com.Famdiba1.Huqsoz.presentation.dialog.ConfirmDialog

@Composable
fun SettingsScreen(onMenu: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    SettingsContent(
        state = state,
        onToggleExpert = settingsViewModel::toggleExpert,
        onToggleMarks = settingsViewModel::toggleMarks,
        onToggleAutoDryRun = settingsViewModel::toggleAutoDryRun,
        onToggleConfirmExit = settingsViewModel::toggleConfirmExit,
        onAskReset = settingsViewModel::askReset,
        onConfirmReset = settingsViewModel::confirmReset,
        onDismissReset = settingsViewModel::dismissReset,
        onMenu = onMenu
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onToggleExpert: () -> Unit,
    onToggleMarks: () -> Unit,
    onToggleAutoDryRun: () -> Unit,
    onToggleConfirmExit: () -> Unit,
    onAskReset: () -> Unit,
    onConfirmReset: () -> Unit,
    onDismissReset: () -> Unit,
    onMenu: () -> Unit
) {
    val scroll = rememberScrollState()
    ScreenBackground(
        overlay = listOf(
            SurfaceSoft.copy(alpha = 0.93f),
            GlassTint.copy(alpha = 0.93f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onMenu,
                    height = 48.dp
                )
                Text(
                    text = stringResource(R.string.settings_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scroll),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SettingRow(
                    title = stringResource(R.string.setting_expert),
                    hint = stringResource(R.string.setting_expert_hint),
                    checked = state.expertBoards,
                    onToggle = onToggleExpert
                )
                SettingRow(
                    title = stringResource(R.string.setting_marks),
                    hint = stringResource(R.string.setting_marks_hint),
                    checked = state.flavorMarks,
                    onToggle = onToggleMarks
                )
                SettingRow(
                    title = stringResource(R.string.setting_auto_dry),
                    hint = stringResource(R.string.setting_auto_dry_hint),
                    checked = state.autoDryRun,
                    onToggle = onToggleAutoDryRun
                )
                SettingRow(
                    title = stringResource(R.string.setting_confirm_exit),
                    hint = stringResource(R.string.setting_confirm_exit_hint),
                    checked = state.confirmExit,
                    onToggle = onToggleConfirmExit
                )

                Spacer(modifier = Modifier.size(4.dp))

                SecondaryButton(
                    text = stringResource(R.string.reset_progress),
                    onClick = onAskReset,
                    modifier = Modifier.fillMaxWidth(),
                    height = 54.dp,
                    accent = Raspberry,
                    labelColor = Raspberry
                )

                Text(
                    text = stringResource(R.string.reset_hint, state.stars, state.tokens),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                )
            }
        }

        if (state.resetOpen) {
            ConfirmDialog(
                title = stringResource(R.string.confirm_reset_title),
                body = stringResource(R.string.confirm_reset_body),
                confirmLabel = stringResource(R.string.reset),
                dismissLabel = stringResource(R.string.cancel),
                onConfirm = onConfirmReset,
                onDismiss = onDismissReset
            )
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    hint: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    val stateLabel = if (checked) stringResource(R.string.state_on) else stringResource(R.string.state_off)
    ClayCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = SurfaceLight.copy(alpha = 0.88f),
        borderColor = Copper.copy(alpha = 0.35f),
        radius = 22.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .semantics { stateDescription = stateLabel },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = hint,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = VanillaCream,
                    checkedTrackColor = Pistachio,
                    uncheckedThumbColor = VanillaCream,
                    uncheckedTrackColor = DisabledFill
                )
            )
        }
    }
}
