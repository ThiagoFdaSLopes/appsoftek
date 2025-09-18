package com.grupo.appsoftek.ui.theme.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appsoftek.R

@Composable
fun LoginScreen(
    onLogin: (String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onErrorDismiss: () -> Unit = {}
) {
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    
    val canProceed = password.isNotEmpty() && !isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E3A8A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Icon(
                painter = painterResource(id = R.drawable.softtek_logo),
                contentDescription = "Logo Softtek",
                tint = Color.Unspecified,
                modifier = Modifier.size(120.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Título
            Text(
                text = "Bem-vindo de volta!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Digite sua senha para continuar",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Card com formulário
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Digite sua Senha",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E3A8A)
                        )
                    )
                    
                    // Campo de senha
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Senha") },
                        placeholder = { Text("Digite sua senha") },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (showPassword) "Ocultar senha" else "Mostrar senha"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Mensagem de erro
                    if (errorMessage != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = errorMessage,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(onClick = onErrorDismiss) {
                                    Text("OK")
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Botão de entrar
                    Button(
                        onClick = { onLogin(password) },
                        enabled = canProceed,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8DC63F),
                            disabledContainerColor = Color.Gray
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = if (isLoading) "Entrando..." else "Entrar",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Informação sobre o ID
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ℹ️ Informação",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Seu ID único foi gerado automaticamente e está salvo com segurança. Você só precisa lembrar da sua senha.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}
