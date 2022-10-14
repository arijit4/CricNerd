package com.offbyabit.cricnerd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.offbyabit.cricnerd.ui.theme.CricNerdTheme

class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CricNerdTheme {
                navController = rememberNavController()
                Navigation(navController)
            }
        }
    }
}

