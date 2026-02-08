package com.example.sleephelper.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sleephelper.ui.home.HomeScreen
import com.example.sleephelper.ui.player.PlayerScreen
import com.example.sleephelper.ui.recorder.RecorderScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Player : Screen("player/{audioId}") {
        fun createRoute(audioId: String) = "player/$audioId"
    }
    data object Recorder : Screen("recorder")
}

@Composable
fun SleepHelperNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300))
        }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onAudioClick = { audioId ->
                    navController.navigate(Screen.Player.createRoute(audioId))
                },
                onRecordClick = {
                    navController.navigate(Screen.Recorder.route)
                }
            )
        }
        composable(Screen.Player.route) { backStackEntry ->
            val audioId = backStackEntry.arguments?.getString("audioId")
            PlayerScreen(
                audioId = audioId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.Recorder.route) {
            RecorderScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
