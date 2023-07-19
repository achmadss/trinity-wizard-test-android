package com.achmadss.trinitywizardstechnicaltest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.achmadss.trinitywizardstechnicaltest.ui.pages.edit.routeEdit
import com.achmadss.trinitywizardstechnicaltest.ui.pages.main.routeMain
import com.achmadss.trinitywizardstechnicaltest.ui.theme.TrinityWizardsTechnicalTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            TrinityWizardsTechnicalTestTheme {
                NavHost(
                    navController = navHostController,
                    startDestination = Routes.MAIN
                ) {
                    routeMain(navHostController)
                    routeEdit(navHostController)
                }
            }
        }
    }
}
