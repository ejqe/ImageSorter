package com.ejqe.imagesorter.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ejqe.imagesorter.presentation.ResultScreen
import com.ejqe.imagesorter.presentation.SorterScreen
import com.ejqe.imagesorter.presentation.SorterViewModel
import com.ejqe.imagesorter.presentation.ui.theme.ImageSorterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageSorterTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "sorter") {
                    composable("sorter") {
                        val viewModel: SorterViewModel = viewModel()
                        SorterScreen(
                            onDialogClick = {
                                navController.navigate("result");
                                viewModel.updateShowDialog(false)
                            },
                            viewModel = viewModel
                        )
                    }

                    composable("result"){
                        val viewModel: SorterViewModel = viewModel()
                        ResultScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

