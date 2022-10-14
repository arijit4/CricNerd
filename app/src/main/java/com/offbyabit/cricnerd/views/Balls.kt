package com.offbyabit.cricnerd.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShowBall(run: String) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(40.dp)
            .padding(4.dp)
            .clip(CircleShape)
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Text(text = run, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun D() {
    ShowBallCard("")
}

@Composable
fun ShowBallCard(data: String) {
    Box(modifier = Modifier) {
        Card(
            shape = RoundedCornerShape(15.dp)
        ) {
            if (data.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .padding(4.dp)
                ) {
                    items(data.split(",")) { run ->
                        ShowBall(run = run)
                    }
                }
            } else {
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = "No data to show.",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}