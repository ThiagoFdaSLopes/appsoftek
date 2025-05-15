package com.grupo.appsoftek.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ActionButtons(
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    isSubmitEnabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onBack,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF05285E)
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Voltar")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onSubmit,
            enabled = isSubmitEnabled,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF05285E),
                disabledContainerColor = Color.Gray
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Responder")
        }
    }
}