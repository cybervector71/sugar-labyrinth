package com.Famdiba1.Huqsoz.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Famdiba1.Huqsoz.R
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.TextPrimary
import com.Famdiba1.Huqsoz.core.theme.VanillaCream
import com.Famdiba1.Huqsoz.core.ui.PrimaryButton
import com.Famdiba1.Huqsoz.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        containerColor = VanillaCream,
        titleContentColor = TextPrimary,
        title = {
            Text(
                text = stringResource(R.string.pause_title),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text = stringResource(R.string.pause_body),
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PrimaryButton(
                    text = stringResource(R.string.resume),
                    onClick = onResume,
                    modifier = Modifier.fillMaxWidth(),
                    height = 52.dp
                )
                SecondaryButton(
                    text = stringResource(R.string.restart),
                    onClick = onRestart,
                    modifier = Modifier.fillMaxWidth(),
                    accent = Copper
                )
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onMenu,
                    modifier = Modifier.fillMaxWidth(),
                    accent = Copper
                )
            }
        }
    )
}
