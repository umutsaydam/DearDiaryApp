package com.umutsaydam.deardiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.umutsaydam.deardiary.presentation.navigation.MainNavHost
import com.umutsaydam.deardiary.ui.theme.DearDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DearDiaryTheme {
                MainNavHost()
            }
        }
    }
}