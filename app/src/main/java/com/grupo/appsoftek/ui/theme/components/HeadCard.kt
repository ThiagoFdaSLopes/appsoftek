package com.grupo.appsoftek.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appsoftek.R
import com.grupo.appsoftek.ui.theme.Bluettek

@Composable
fun HeadCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth().size(width = 300.dp, height = 150.dp),
        color = Color.White,
        shadowElevation = 32.dp,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(
            topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 5.dp, top = 5.dp
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.iconcheckin),
                    contentDescription = "Ícone do check-in",
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Check-in diário",
                    color = Bluettek,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.offset(
                        x = 0.dp, y = (-5.dp)
                    )
                )
            }
            Text(
                text = "Como você está se sentindo hoje?\nEscolha o emogi que melhor representa seu humor.",
                color = Bluettek,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )
            Emogis(
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Row {
                Image(
                    painter = painterResource(id = R.drawable.icon_status_time),
                    contentDescription = "Ícone do statuso de tempo",
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "15 minutos atrás", color = Bluettek, fontSize = 12.sp
                )
            }

        }
    }
}

@Composable
fun HeadCardMsgMotiva(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth().size(width = 300.dp, height = 150.dp),
        color = Color.White,
        shadowElevation = 32.dp,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(
            topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 5.dp, top = 5.dp
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_msg_motiva),
                    contentDescription = "Ícone mensagem motivacional",
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Mensagem Motivacional",
                    color = Bluettek,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.offset(
                        x = 0.dp, y = (-5.dp)
                    )
                )
            }
            Text(
                text = "“A maior glória não está em nunca cair, mas em se\n levantar cada vez que caímos.” - Confúcio ",
                color = Bluettek,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )
            Row {
                Image(
                    painter = painterResource(id = R.drawable.icon_status_time),
                    contentDescription = "Ícone do statuso de tempo",
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "1 hora atrás", color = Bluettek, fontSize = 12.sp
                )
            }

        }
    }
}

@Composable
fun HeadCardApoio(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth().size(width = 300.dp, height = 150.dp),
        color = Color.White,
        shadowElevation = 32.dp,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(
            topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 5.dp, top = 5.dp
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_rede_apoio),
                    contentDescription = "Ícone Rede de Apoio",
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Rede de Apoio",
                    color = Bluettek,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.offset(
                        x = 0.dp, y = (-5.dp)
                    )
                )
            }
            Text(
                text = "“A conversa com profissionais qualificados pode ajudar\n nos momentos difíceis. Confira nossos canais de escuta.",
                color = Bluettek,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )
            Row {
                Image(
                    painter = painterResource(id = R.drawable.icon_status_time),
                    contentDescription = "Ícone do statuso de tempo",
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "Há mais de uma hora", color = Bluettek, fontSize = 12.sp
                )
            }

        }
    }
}

@Preview
@Composable
private fun HeadCardPreview() {
    HeadCard()
}

@Preview
@Composable
private fun HeadCardMsgMotivaPreview() {
    HeadCardMsgMotiva()
}

@Preview
@Composable
private fun HeadCardApoioPreview() {
    HeadCardApoio()
}