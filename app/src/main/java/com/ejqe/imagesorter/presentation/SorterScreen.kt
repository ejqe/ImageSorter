package com.ejqe.imagesorter.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejqe.imagesorter.R
import com.ejqe.imagesorter.data.MasterList
import com.ejqe.imagesorter.ui.theme.ImageSorterTheme



@Composable
fun SorterScreen() {


    val viewModel: SorterViewModel = viewModel()
    val playerNameA = viewModel.currentPair.value.first
    val playerNameB = viewModel.currentPair.value.second

    val playerScoreA = viewModel.players.find { it.name == playerNameA }!!.score.toInt()
    val playerScoreB = viewModel.players.find { it.name == playerNameB }!!.score.toInt()

    val matchNo = (viewModel.allMatches.indexOf(viewModel.currentPair.value) + 1).toString()




    if (viewModel.showDialog.value){
        PopupDialog()
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

            Text(
                text = "Match #$matchNo",
                fontSize = 32.sp)
            Spacer(modifier = Modifier.height(60.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                progress = viewModel.progress.value
            )

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {

                CardItem(
                    modifier = Modifier
                        .weight(1f),
                    text = playerNameA,
                    textScore = playerScoreA,
                    onClick = { if (viewModel.isClickable) {viewModel.onSelect(1) } }
                )
                CardItem(
                    modifier = Modifier
                        .weight(1f),
                    text = playerNameB,
                    textScore = playerScoreB,
                    onClick = { if (viewModel.isClickable) {viewModel.onSelect(2) } }
                )
            }
            Button(
                onClick = { if (viewModel.isClickable) {viewModel.onSelect(3) } },
                modifier = Modifier
                    .width(120.dp)
                    .padding(8.dp),
            ) {

                Text(text = "Draw")
            }

        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(modifier: Modifier, text: String, textScore: Int, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .height(250.dp),
        shape = RoundedCornerShape(10),
        onClick = onClick

    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(shape = RoundedCornerShape(10)),
                painter = painterResource(R.drawable.image),
                contentDescription = "Image"
            )

            Text(
                text = text,
                modifier = Modifier.padding(
                    horizontal = 8.dp, vertical = 4.dp
                )
            )

            Text(
                text = textScore.toString(),
                modifier = Modifier.padding(
                    horizontal = 8.dp, vertical = 4.dp
                )
            )
        }
    }
}

@Composable

fun PopupDialog() {
    val viewModel: SorterViewModel = viewModel()
    val showDialog = viewModel.showDialog.value

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = "Sort Finished") },
            text = { Text(text = "Click OK to see the results") },
            confirmButton = {
                Button(onClick = {  }) {
                    Text(text = "OK")
                }
            }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun SorterScreenPreview() {
    ImageSorterTheme {
        SorterScreen()
    }
}