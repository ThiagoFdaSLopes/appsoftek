package com.grupo.appsoftek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.grupo.appsoftek.ui.theme.AppSoftekTheme
import com.grupo.appsoftek.ui.theme.view.RiskAssessmentScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppSoftekTheme {
                // Chamando a sua screen de Avaliação de Riscos
                RiskAssessmentScreen()
            }
        }
    }
}
