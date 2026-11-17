package com.Famdiba1.Huqsoz.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.Famdiba1.Huqsoz.core.theme.TextSecondary

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextSecondary
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        modifier = modifier
    )
}
