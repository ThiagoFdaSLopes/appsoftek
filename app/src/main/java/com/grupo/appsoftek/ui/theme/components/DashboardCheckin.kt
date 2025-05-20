package com.grupo.appsoftek.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appsoftek.ui.theme.Bluettek
import com.grupo.appsoftek.ui.theme.Greenttek2

@Composable
fun DashboardCheckin(
    modifier: Modifier = Modifier, day: Int
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp, vertical = 18.dp)
            .size(width = 134.dp, height = 116.dp),
        color = Greenttek2,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            horizontalAlignment = CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.toString(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Bluettek
            )

            Spacer(modifier = Modifier.width(50.dp))

            Text(
                text = "Dias de check-in", fontSize = 12.sp, color = Bluettek
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DashboardCheckinPreview() {
    DashboardCheckin(day = 7)
}