package com.grupo.appsoftek.ui.theme.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grupo.appsoftek.ui.theme.Greenttek
import com.grupo.appsoftek.ui.theme.components.CardSupport
import com.grupo.appsoftek.ui.theme.components.HeadDefault

@Composable
fun SupportNetworking(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Greenttek
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeadDefault(
                title = "Rede de apoio",
                subtitle = "Um lugar seguro e dedicado a te apoiar"
            )

            Spacer(modifier = Modifier.height(40.dp))

            CardSupport()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SupportNetworkingPreview() {
    SupportNetworking()
}