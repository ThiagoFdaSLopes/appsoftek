package com.grupo.appsoftek.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.grupo.appsoftek.ui.theme.view.QuestionnaireTheme

@Composable
fun QuestionnaireScreen(
    title: String = "",
    questions: List<Question>,
    theme: QuestionnaireTheme,
    showNotificationIcon: Boolean = true,
    onBackPressed: () -> Unit = {},
    onFinished: (List<String?>) -> Unit = {}
) {
    // Current question index and selected answers
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswers by remember { mutableStateOf(List<String?>(questions.size) { null }) }

    // Current question and selected answer
    val currentQuestion = questions[currentQuestionIndex]
    val selectedOption = selectedAnswers[currentQuestionIndex]

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
                    containerColor = theme.cardBackgroundColor
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
                        color = theme.questionTextColor,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Options for current question
                    currentQuestion.options.forEach { option ->
                        val isSelected = option == selectedOption
                        val backgroundColor = if (isSelected) theme.selectedOptionColor else theme.unselectedOptionColor
                        val textColor = if (isSelected) theme.selectedTextColor else theme.unselectedTextColor
                        val borderColor = if (isSelected) theme.selectedOptionColor else theme.borderColor

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                // Update the selected answer for the current question
                                val newAnswers = selectedAnswers.toMutableList().apply {
                                    this[currentQuestionIndex] = option
                                }
                                selectedAnswers = newAnswers
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .border(
                                    width = 1.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = backgroundColor,
                                contentColor = textColor
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = option,
                                fontSize = 16.sp
                            )
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
                        containerColor = theme.navigationButtonColor,
                        contentColor = theme.navigationButtonTextColor
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
                        containerColor = theme.navigationButtonColor,
                        contentColor = theme.navigationButtonTextColor
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