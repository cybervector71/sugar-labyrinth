package com.Famdiba1.Huqsoz.presentation.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.TextPrimary
import com.Famdiba1.Huqsoz.core.theme.VanillaCream
import com.Famdiba1.Huqsoz.core.ui.PrimaryButton
import com.Famdiba1.Huqsoz.core.ui.SecondaryButton

@Composable
fun ConfirmDialog(
    title: String,
    body: String,
    confirmLabel: String,
    dismissLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = VanillaCream,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
        },
        confirmButton = {
            PrimaryButton(
                text = confirmLabel,
                onClick = onConfirm,
                modifier = Modifier,
                height = 50.dp
            )
        },
        dismissButton = {
            SecondaryButton(
                text = dismissLabel,
                onClick = onDismiss,
                accent = Copper
            )
        }
    )
}
