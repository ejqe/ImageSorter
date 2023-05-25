package com.ejqe.imagesorter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ejqe.imagesorter.presentation.ResultScreen
import com.ejqe.imagesorter.presentation.SorterScreen
import com.ejqe.imagesorter.presentation.SorterViewModel
import com.ejqe.imagesorter.ui.theme.ImageSorterTheme

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

