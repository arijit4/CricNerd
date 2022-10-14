package com.offbyabit.cricnerd.screens.match_detail_screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailCard(key: String, detail: String, shouldSpace: Boolean = true) {
    Row {
        Text(
            modifier = Modifier.fillMaxWidth(0.3f),
            text = key,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = detail,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    if (shouldSpace) {
        Spacer(modifier = Modifier.height(8.dp))
    }
}