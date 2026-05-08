package com.lucy.myfoodaccessapp2.ui.Navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucy.myfoodaccessapp2.ui.Screens.Authentication.LoginScreen
import com.lucy.myfoodaccessapp2.ui.Screens.Authentication.SignupScreen
import com.lucy.myfoodaccessapp2.ui.Screens.Home.HomeScreen
import com.lucy.myfoodaccessapp2.ui.Screens.OnboardingScreen.OnboardingScreen
import com.lucy.myfoodaccessapp2.ui.Screens.SplashScreens.SplashScreen
import com.lucy.myfoodaccessapp2.ui.Screens.Food.FoodForm

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController, 
        startDestination = ROUTES.SPLASH.path,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        }
    ) {
        composable(ROUTES.SPLASH.path) {
            SplashScreen(navController)
        }
        composable(ROUTES.ONBOARDING.path) {
            OnboardingScreen(navController)
        }
        composable(ROUTES.LOGIN.path) {
            LoginScreen(navController)
        }
        composable(ROUTES.SIGNUP.path) {
            SignupScreen(navController)
        }
        composable(ROUTES.HOME.path) {
            HomeScreen(navController)
        }
        composable(ROUTES.ADD_FOOD.path) {
            FoodForm(navController)
        }
    }
}
