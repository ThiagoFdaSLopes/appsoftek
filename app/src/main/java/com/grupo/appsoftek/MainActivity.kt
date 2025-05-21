package com.grupo.appsoftek

//import com.grupo.appsoftek.ui.theme.view.ResourcesScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.grupo.appsoftek.ui.theme.view.ClimaQuestionScreen
import com.grupo.appsoftek.ui.theme.view.DashboardScreen
import com.grupo.appsoftek.ui.theme.view.MoodTrackingScreen
import com.grupo.appsoftek.ui.theme.view.NotificationsScreen
import com.grupo.appsoftek.ui.theme.view.ProductivityQuestionScreen
import com.grupo.appsoftek.ui.theme.view.QuestionsComunicationScreen
import com.grupo.appsoftek.ui.theme.view.QuestionsLeadersheapScreen
import com.grupo.appsoftek.ui.theme.view.RiskAssessmentScreen
import com.grupo.appsoftek.ui.theme.view.SectionDetailScreen
import com.grupo.appsoftek.ui.theme.view.SupportNetworking
import com.grupo.appsoftek.ui.theme.view.WorkloadQuestionScreen
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionResponseViewModel
import kotlinx.coroutines.launch

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
    object Assessment : Screen("assessment", "Avaliação", R.drawable.ic_assessment)
    object Dashboard : Screen("dashboard", "Dashboard", R.drawable.ic_wellbeing)
    object Resources : Screen("resources", "Notificações", R.drawable.ic_resources)
    object Support : Screen("support", "Apoio", R.drawable.ic_support)


    object SectionDetail : Screen("section/{sectionTitle}", "Detalhe", R.drawable.hand_heart) {
        fun createRoute(sectionTitle: String) = "section/$sectionTitle"
    }

    // Rota para a tela de perguntas sobre carga de trabalho
    object WorkloadQuestions : Screen("workload", "Carga de Trabalho", R.drawable.hand_heart)

    // Nova rota para a tela de perguntas sobre produtividade
    object ProductivityQuestions : Screen("productivity", "Produtividade", R.drawable.hand_heart)

    // Nova rota para a tela de perguntas sobre Clima
    object ClimaQuestionsScreen : Screen("clima", "Clima", R.drawable.hand_heart)

    // Nova rota para a tela de perguntas sobre comunication
    object QuestionsComunicationScreen :
        Screen("comunication", "Comunicação", R.drawable.hand_heart)

    // Nova rota para a tela de perguntas sobre comunication
    object QuestionsLeadersheapScreen : Screen("liderança", "Liderança", R.drawable.hand_heart)

    // Nova rota para a tela de perguntas sobre comunication
    object BemEstarEmocional : Screen("Bem-estar emocional", "mood_tracking", R.drawable.hand_heart)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val qrVm: QuestionResponseViewModel = viewModel()
    val sections by qrVm.sectionsFlow.collectAsState()

    val scope = rememberCoroutineScope()

    // Lista das telas principais para a barra de navegação
    val mainScreens = listOf(
        Screen.Assessment,
        Screen.Dashboard,
        Screen.Resources,
        Screen.Support
    )

    // Rotas onde NÃO queremos mostrar o TopBar
    val noTopBarRoutes = listOf(
        Screen.Dashboard.route,
        Screen.Resources.route,
        Screen.Support.route
    )

    // Exibir TopBar quando a rota atual NÃO estiver em noTopBarRoutes
    val showTopBar by remember(currentRoute) {
        derivedStateOf { currentRoute !in noTopBarRoutes }
    }


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
            currentRoute == Screen.Dashboard.route -> Screen.Dashboard.title
            currentRoute == Screen.Resources.route -> Screen.Resources.title
            currentRoute == Screen.Support.route -> Screen.Support.title
            currentRoute == Screen.WorkloadQuestions.route -> Screen.WorkloadQuestions.title
            currentRoute == Screen.ClimaQuestionsScreen.route -> Screen.ClimaQuestionsScreen.title
            currentRoute == Screen.QuestionsComunicationScreen.route -> Screen.QuestionsComunicationScreen.title
            currentRoute == Screen.QuestionsLeadersheapScreen.route -> Screen.QuestionsLeadersheapScreen.title
            currentRoute == Screen.ProductivityQuestions.route -> Screen.ProductivityQuestions.title
            currentRoute == Screen.BemEstarEmocional.route -> Screen.BemEstarEmocional.title
            else -> ""
        }
    }

    // Determina se devemos mostrar a barra de navegação inferior
//    val showBottomBar by remember(currentRoute) {
//        derivedStateOf {
//            mainScreens.any { it.route == currentRoute }
//        }
//    }
    val showBottomBar by remember(currentRoute) {
        derivedStateOf { currentRoute in mainScreens.map { it.route } }
    }

    Scaffold(
        topBar = {
            if (showTopBar) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = currentScreenTitle,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        if (currentRoute != null && currentRoute !in mainScreens.map { it.route }) {
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
            }
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
                    sections = sections,
                    onSectionClick = { sectionTitle ->
                        // mapeia o tipo de questionário a partir do título
                        val questionnaireType = when (sectionTitle) {
                            "Bem-estar emocional" -> "mood_tracking"
                            "Carga de trabalho" -> "carga-de-trabalho"
                            "Produtividade" -> "produtividade"
                            "Clima" -> "clima"
                            "Comunicação" -> "comunicacao"
                            "Liderança" -> "liderança"
                            else -> ""
                        }
                        // checa e navega ou mostra Toast
                        scope.launch {
                            if (qrVm.hasAnsweredToday(questionnaireType)) {
                                Toast.makeText(
                                    navController.context,
                                    "Você já respondeu '$sectionTitle' hoje.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // só navega se ainda não respondeu
                                when (sectionTitle) {
                                    "Dashboard" -> navController.navigate(Screen.Dashboard.route)
                                    "Carga de trabalho" -> navController.navigate(Screen.WorkloadQuestions.route)
                                    "Produtividade" -> navController.navigate(Screen.ProductivityQuestions.route)
                                    "Clima" -> navController.navigate(Screen.ClimaQuestionsScreen.route)
                                    "Comunicação" -> navController.navigate(Screen.QuestionsComunicationScreen.route)
                                    "Liderança" -> navController.navigate(Screen.QuestionsLeadersheapScreen.route)
                                    "Apoio" -> navController.navigate(Screen.Support.route)
                                    "Notificações" -> navController.navigate(Screen.Resources.route)
                                    "Bem-estar emocional" -> navController.navigate(Screen.BemEstarEmocional.route)
                                    else -> navController.navigate(
                                        Screen.SectionDetail.createRoute(
                                            sectionTitle
                                        )
                                    )
                                }
                            }
                        }
                    }
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen()
            }

            composable(Screen.Resources.route) {
                NotificationsScreen(navController = navController)
            }

            composable(Screen.Support.route) {
                SupportNetworking()
            }

            // Rota para a tela de perguntas sobre carga de trabalho
            composable(Screen.BemEstarEmocional.route) {
                MoodTrackingScreen(
                    onBackPressed = { navController.popBackStack() },
                    navController = navController
                )
            }


            // Rota para a tela de perguntas sobre carga de trabalho
            composable(Screen.WorkloadQuestions.route) {
                WorkloadQuestionScreen(
                    onBackPressed = { navController.popBackStack() },
                    onFinished = { answers ->
                        // Aqui você pode salvar as respostas ou navegar para outra tela
                        navController.popBackStack()
                        // Você pode querer avisar o usuário que as respostas foram salvas
                        Toast.makeText(
                            navController.context,
                            "Respostas sobre carga de trabalho salvas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }

            // Nova rota para a tela de perguntas sobre produtividade
            composable(Screen.ProductivityQuestions.route) {
                ProductivityQuestionScreen(
                    onBackPressed = { navController.popBackStack() },
                    onFinished = { answers ->
                        // Aqui você pode salvar as respostas ou navegar para outra tela
                        navController.popBackStack()
                        // Você pode querer avisar o usuário que as respostas foram salvas
                        Toast.makeText(
                            navController.context,
                            "Respostas sobre produtividade salvas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }

            // Rota para a tela de perguntas sobre clima
            composable(Screen.ClimaQuestionsScreen.route) {
                ClimaQuestionScreen(
                    onBackPressed = { navController.popBackStack() },
                    onFinished = { answers ->
                        // Aqui você pode salvar as respostas ou navegar para outra tela
                        navController.popBackStack()
                        // Você pode querer avisar o usuário que as respostas foram salvas
                        Toast.makeText(
                            navController.context,
                            "Respostas sobre clima salvas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }

            // Rota para a tela de perguntas sobre Comunicação
            composable(Screen.QuestionsComunicationScreen.route) {
                QuestionsComunicationScreen(
                    onBackPressed = { navController.popBackStack() },
                    onFinished = { answers ->
                        // Aqui você pode salvar as respostas ou navegar para outra tela
                        navController.popBackStack()
                        // Você pode querer avisar o usuário que as respostas foram salvas
                        Toast.makeText(
                            navController.context,
                            "Respostas sobre comunicação salvas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }

            // Rota para a tela de perguntas sobre Liderança
            composable(Screen.QuestionsLeadersheapScreen.route) {
                QuestionsLeadersheapScreen(
                    onBackPressed = { navController.popBackStack() },
                    onFinished = { answers ->
                        // Aqui você pode salvar as respostas ou navegar para outra tela
                        navController.popBackStack()
                        // Você pode querer avisar o usuário que as respostas foram salvas
                        Toast.makeText(
                            navController.context,
                            "Respostas sobre liderança salvas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
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
                        contentDescription = screen.title,
                        modifier = Modifier.size(26.dp)

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