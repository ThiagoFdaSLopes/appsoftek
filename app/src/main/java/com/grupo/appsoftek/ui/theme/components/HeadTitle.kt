package com.grupo.appsoftek.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appsoftek.R
import com.grupo.appsoftek.ui.theme.Bluettek
import com.grupo.appsoftek.ui.theme.Whitettek

@Composable
fun HeadTitle(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = Bluettek,
        shadowElevation = 4.dp,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(
            bottomStart = 24.dp, bottomEnd = 24.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_bell),
                contentDescription = "Ícone do sino",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Seus lembretes",
                    color = Whitettek, //Cor branca
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "Mantenha-se atualizado com as novidades",
                    color = Whitettek,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun HeadTitlePreview() {
    HeadTitle()
}