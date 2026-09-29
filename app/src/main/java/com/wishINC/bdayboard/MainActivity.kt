package com.wishINC.bdayboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wishINC.bdayboard.ui.screens.HomeScreen
import com.wishINC.bdayboard.ui.theme.BdayBoardTheme
import com.wishINC.bdayboard.viewmodel.BirthdayViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BdayBoardTheme {
                val viewModel: BirthdayViewModel = viewModel()
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}