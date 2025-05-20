package com.grupo.appsoftek.ui.theme.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appsoftek.ui.theme.Bluettek
import com.grupo.appsoftek.ui.theme.components.HeadCard
import com.grupo.appsoftek.ui.theme.components.HeadCardApoio
import com.grupo.appsoftek.ui.theme.components.HeadCardMsgMotiva
import com.grupo.appsoftek.ui.theme.components.HeadTitle

@Composable
fun NotificationsScreen(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HeadTitle(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Hoje",
            color = Bluettek,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        HeadCard(modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(10.dp))
        HeadCardMsgMotiva(modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(10.dp))
        HeadCardApoio(modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(30.dp))

//        Text(
//            text = "Ontem",
//            color = Bluettek,
//            fontSize = 18.sp,
//            fontWeight = FontWeight.SemiBold,
//            modifier = Modifier.padding(start = 20.dp)
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        HeadCard(modifier = Modifier.padding(horizontal = 20.dp))
//        Spacer(modifier = Modifier.height(10.dp))
//        HeadCardMsgMotiva(modifier = Modifier.padding(horizontal = 20.dp))
//        Spacer(modifier = Modifier.height(10.dp))
//        HeadCardApoio(modifier = Modifier.padding(horizontal = 20.dp))
//        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NotificationsScreenPreview() {
    NotificationsScreen()
}