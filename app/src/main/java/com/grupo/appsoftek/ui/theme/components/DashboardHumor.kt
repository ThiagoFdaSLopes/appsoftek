package com.grupo.appsoftek.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appsoftek.R
import com.grupo.appsoftek.ui.theme.Bluettek
import com.grupo.appsoftek.ui.theme.Whitettek

data class MoodDay(
    val date: String,
    val emojiResourceId: Int
)

@Composable
fun DashboardHumor(
    modifier: Modifier = Modifier,
    moods: List<MoodDay> = emptyList(),
    onCheckInClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp, vertical = 18.dp),
        color = Whitettek,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mood emojis with dates
            Text(
                text = "Humor dos últimos dias",
                color = Bluettek,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
                )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                moods.forEach { mood ->
                    MoodItem(date = mood.date, emojiResourceId = mood.emojiResourceId)
                }
            }
        }
    }
}

@Composable
fun MoodItem(date: String, emojiResourceId: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = emojiResourceId),
            contentDescription = "Mood for $date",
            modifier = Modifier.size(32.dp)
        )

        Text(
            text = date,
            fontSize = 12.sp,
            color = Color.Black.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

// For Preview purposes, sample mood data
@Preview
@Composable
private fun DashboardHumorPreview() {
    val sampleMoods = listOf(
        MoodDay("10/05", R.drawable.icon_fear),
        MoodDay("11/05", R.drawable.icon_exausto),
        MoodDay("13/05", R.drawable.icon_angry),
        MoodDay("14/05", R.drawable.icon_happy),
        MoodDay("15/05", R.drawable.icon_anxious),
        MoodDay("16/05", R.drawable.icon_happy),
        MoodDay("17/05", R.drawable.icon_happy)
    )

    DashboardHumor(moods = sampleMoods)
}