package com.grupo.appsoftek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grupo.appsoftek.ui.theme.AppSoftekTheme
import com.grupo.appsoftek.ui.theme.view.MoodTrackingScreen
import com.grupo.appsoftek.ui.theme.view.ResourcesScreen
import com.grupo.appsoftek.ui.theme.view.SectionDetailScreen
import com.grupo.appsoftek.ui.theme.view.SupportScreen
import com.grupo.appsoftek.ui.theme.view.RiskAssessmentScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppSoftekTheme {
                AppNavigation()
            }
        }
    }
}

// Definindo as rotas do aplicativo
sealed class Screen(val route: String, val title: String, val icon: Int) {
    object Assessment : Screen("assessment", "Avaliação", R.drawable.softtek_logo)
    object MoodTracking : Screen("mood", "Bem-estar", R.drawable.softtek_logo)
    object Resources : Screen("resources", "Recursos", R.drawable.softtek_logo)
    object Support : Screen("support", "Apoio", R.drawable.softtek_logo)

    // Rotas detalhadas
    object SectionDetail : Screen("section/{sectionTitle}", "Detalhe", R.drawable.softtek_logo) {
        fun createRoute(sectionTitle: String) = "section/$sectionTitle"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Lista das telas principais para a barra de navegação
    val mainScreens = listOf(
        Screen.Assessment,
        Screen.MoodTracking,
        Screen.Resources,
        Screen.Support
    )

    // Determina se estamos em uma tela secundária que precisa de botão voltar
    val showBackButton by remember(currentRoute) {
        derivedStateOf {
            currentRoute != null && !mainScreens.any { it.route == currentRoute }
        }
    }

    // Determina o título atual da tela
    val currentScreenTitle = remember(currentRoute) {
        when {
            currentRoute?.startsWith("section/") == true -> {
                val sectionTitle = navBackStackEntry?.arguments?.getString("sectionTitle") ?: ""
                sectionTitle
            }
            currentRoute == Screen.Assessment.route -> Screen.Assessment.title
            currentRoute == Screen.MoodTracking.route -> Screen.MoodTracking.title
            currentRoute == Screen.Resources.route -> Screen.Resources.title
            currentRoute == Screen.Support.route -> Screen.Support.title
            else -> ""
        }
    }

    // Determina se devemos mostrar a barra de navegação inferior
    val showBottomBar by remember(currentRoute) {
        derivedStateOf {
            mainScreens.any { it.route == currentRoute }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = currentScreenTitle,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1E3A8A),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    navController = navController,
                    items = mainScreens
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Assessment.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Assessment.route) {
                RiskAssessmentScreen(
                    onSectionClick = { sectionTitle ->
                        when (sectionTitle) {
                            "Bem-estar emocional" -> navController.navigate(Screen.MoodTracking.route)
                            else -> navController.navigate(Screen.SectionDetail.createRoute(sectionTitle))
                        }
                    }
                )
            }

            composable(Screen.MoodTracking.route) {
                MoodTrackingScreen(
                    onBackPressed = { navController.popBackStack() }
                )
            }

            composable(Screen.Resources.route) {
                ResourcesScreen()
            }

            composable(Screen.Support.route) {
                SupportScreen()
            }

            // Rota para detalhes da seção com argumento
            composable(
                route = Screen.SectionDetail.route,
                arguments = listOf(navArgument("sectionTitle") { type = NavType.StringType })
            ) { backStackEntry ->
                val sectionTitle = backStackEntry.arguments?.getString("sectionTitle") ?: ""
                SectionDetailScreen(sectionTitle = sectionTitle)
            }
        }
    }
}

@Composable
fun BottomNavBar(
    navController: NavController,
    items: List<Screen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Evita múltiplas cópias da mesma destinação no backstack
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Evita múltiplas cópias da mesma destinação no backstack
                        launchSingleTop = true
                        // Restaura o estado quando navegar de volta para destinação
                        restoreState = true
                    }
                }
            )
        }
    }
}