package com.umutsaydam.deardiary

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.umutsaydam.deardiary.presentation.navigation.MainNavHost
import com.umutsaydam.deardiary.ui.theme.DearDiaryTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            DearDiaryTheme {
                MainNavHost()
            }
        }
    }
}