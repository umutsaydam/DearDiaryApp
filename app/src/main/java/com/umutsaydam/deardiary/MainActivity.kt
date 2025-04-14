package com.umutsaydam.deardiary

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.umutsaydam.deardiary.presentation.navigation.MainNavHost
import com.umutsaydam.deardiary.ui.theme.DearDiaryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splash = installSplashScreen()
        enableEdgeToEdge()
        setContent {
            DearDiaryTheme {
                val mainActivityViewModel = hiltViewModel<MainActivityViewModel>()
                val nextRoute = mainActivityViewModel.nextRoute.collectAsState()
                splash.setKeepOnScreenCondition { nextRoute.value == null }
                nextRoute.value?.let { MainNavHost(mainDestination = it) }
            }
        }
    }
}