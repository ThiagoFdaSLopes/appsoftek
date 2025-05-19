package com.grupo.appsoftek.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grupo.appsoftek.R

@Composable
fun Emogis(modifier: Modifier) {
    Row(

    ) {
        Image(
            painter = painterResource(id = R.drawable.bad),
            contentDescription = "Ícone do check-in",
            modifier = Modifier
                .size(32.dp)
                //.align(Alignment.TopStart)
                .offset(x = 5.dp, y = 0.dp),
        )
        Image(
            painter = painterResource(id = R.drawable.icon_fear),
            contentDescription = "Ícone do check-in",
            modifier = Modifier
                .size(32.dp)
                //.align(Alignment.TopStart)
                .offset(x = 5.dp, y = 0.dp),
        )
        Image(
            painter = painterResource(id = R.drawable.icon_exausto),
            contentDescription = "Ícone do check-in",
            modifier = Modifier
                .size(32.dp)
                //.align(Alignment.TopStart)
                .offset(x = 5.dp, y = 0.dp),
        )
        Image(
            painter = painterResource(id = R.drawable.icon_anxious),
            contentDescription = "Ícone do check-in",
            modifier = Modifier
                .size(32.dp)
                //.align(Alignment.TopStart)
                .offset(x = 5.dp, y = 0.dp),
        )
        Image(
            painter = painterResource(id = R.drawable.icon_angry),
            contentDescription = "Ícone do check-in",
            modifier = Modifier
                .size(32.dp)
                //.align(Alignment.TopStart)
                .offset(x = 5.dp, y = 0.dp),
        )
        Image(
            painter = painterResource(id = R.drawable.icon_happy),
            contentDescription = "Ícone do check-in",
            modifier = Modifier
                .size(32.dp)
                //.align(Alignment.TopStart)
                .offset(x = 5.dp, y = 0.dp),
        )
    }
}

@Preview
@Composable
private fun EmogisPreview() {
    Emogis(modifier = Modifier)
}