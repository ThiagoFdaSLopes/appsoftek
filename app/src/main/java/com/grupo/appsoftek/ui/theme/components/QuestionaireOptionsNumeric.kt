package com.grupo.appsoftek.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appsoftek.ui.theme.view.Question
import com.grupo.appsoftek.ui.theme.view.QuestionnaireNumericTheme

@Composable
fun QuestionaireOptionsNumeric(
    title: String = "",
    questions: List<Question>,
    theme: QuestionnaireNumericTheme,
    showNotificationIcon: Boolean = false,
    onBackPressed: () -> Unit = {},
    onFinished: (List<String?>) -> Unit = {}
) {
    // Current question index and selected answers
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswers by remember { mutableStateOf(List<String?>(questions.size) { null }) }

    // Current question and selected answer
    val currentQuestion = questions[currentQuestionIndex]
    val selectedOption = selectedAnswers[currentQuestionIndex]

    // Cor azul forte para a opção selecionada
    val selectedBlue = Color(0xFF0A3977)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.backgroundColor)
    ) {
        // Notification icon
        if (showNotificationIcon) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificações",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(28.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Optional title
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Question progress indicator
            Text(
                text = "Pergunta ${currentQuestionIndex + 1} de ${questions.size}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentQuestion.question,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0A3977),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Define tamanho fixo para todos os botões
                    val buttonSize = 56.dp

                    // Numeric options (1-5) in a row
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Define colors for the option boxes
                        val colors = listOf(
                            Color(0xFFE63946), // Red for 1
                            Color(0xFFE74C3C), // Lighter red for 2
                            Color(0xFFFF9F43), // Orange for 3
                            Color(0xFFBDCF32), // Yellow-green for 4
                            Color(0xFF4CAF50)  // Green for 5
                        )

                        // Create numeric buttons 1 through 5
                        for (i in 1..5) {
                            val optionValue = i.toString()
                            val isSelected = optionValue == selectedOption

                            // Determine button color based on selection status
                            val buttonColor = if (isSelected) selectedBlue else colors[i - 1]

                            Box(
                                modifier = Modifier
                                    .size(buttonSize)
                                    .padding(horizontal = 2.dp)
                            ) {
                                Button(
                                    onClick = {
                                        // Update the selected answer for the current question
                                        val newAnswers = selectedAnswers.toMutableList().apply {
                                            this[currentQuestionIndex] = optionValue
                                        }
                                        selectedAnswers = newAnswers
                                    },
                                    modifier = Modifier
                                        .size(buttonSize)
                                        .align(Alignment.Center),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = buttonColor
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                        0.dp
                                    )
                                ) {
                                    Text(
                                        text = optionValue,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Navigation buttons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Back button
                Button(
                    onClick = {
                        if (currentQuestionIndex > 0) {
                            currentQuestionIndex--
                        } else {
                            onBackPressed()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A3977),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Voltar")
                }

                // Next/Finish button
                Button(
                    onClick = {
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                        } else {
                            // Finished all questions
                            onFinished(selectedAnswers)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A3977),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    // Optionally disable the button if no option is selected
                    enabled = selectedOption != null
                ) {
                    Text(
                        if (currentQuestionIndex < questions.size - 1) "Avançar" else "Finalizar"
                    )
                }
            }
        }
    }
}