package com.ejqe.imagesorter.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejqe.imagesorter.R
import com.ejqe.imagesorter.ui.theme.ImageSorterTheme


@Composable
fun SorterScreen(
    onDialogClick: () -> Unit,
    viewModel: SorterViewModel,
) {

    val state = viewModel.state.collectAsState().value


    val playerNameA = state.currentPair.first
    val playerNameB = state.currentPair.second

    val playerScoreA = viewModel.players.find { it.name == playerNameA }!!.score.toInt()
    val playerScoreB = viewModel.players.find { it.name == playerNameB }!!.score.toInt()
    val playerImageA = viewModel.players.find { it.name == playerNameA }!!.image
    val playerImageB = viewModel.players.find { it.name == playerNameB }!!.image

    val matchNo = (viewModel.allMatches.indexOf(state.currentPair) + 1).toString()




    if (state.showDialog) {
        PopupDialog(onDialogClick = onDialogClick)
    }


    LinearProgressIndicator(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        progress = state.progress
    )
    CardPair(
        imageA = playerImageA,
        imageB = playerImageB,
        nameA = playerNameA,
        nameB = playerNameB,
        matchNo = matchNo,
        onClickA = { if (viewModel.isClickable) { viewModel.onSelect(1) } },
        onClickB = { if (viewModel.isClickable) { viewModel.onSelect(2) } },
        onClickButton = { if (viewModel.isClickable) { viewModel.onSelect(3) } }
    )


   /* Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Text(
            text = "Match #$matchNo",
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(60.dp))

        LinearProgressIndicator(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            progress = state.progress
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
                image = playerImageA,
                text = playerNameA,
                onClick = {
                    if (viewModel.isClickable) {
                        viewModel.onSelect(1)
                    }
                }
            )
            CardItem(
                modifier = Modifier
                    .weight(1f),
                image = playerImageB,
                text = playerNameB,
                onClick = {
                    if (viewModel.isClickable) {
                        viewModel.onSelect(2)
                    }
                }
            )
        }
        Button(
            onClick = {
                if (viewModel.isClickable) {
                    viewModel.onSelect(3)
                }
            },
            modifier = Modifier
                .width(120.dp)
                .padding(8.dp),
        ) {

            Text(text = "Draw")
        }

    }*/
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(
    modifier: Modifier,
    image: Int,
    text: String,
    onClick: () -> Unit
) {

    val imageHeight = 350

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight.dp)
    ) {

        Card(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .aspectRatio(0.6f),
            shape = RoundedCornerShape(10),
            onClick = onClick

        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {

                Text(
                    text = text,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .align(Alignment.BottomCenter),
                    textAlign = TextAlign.Center
                )


            }
        }
        Image(
            painter = painterResource(image),
            contentDescription = "Image",
            modifier = Modifier
                .padding(4.dp)
                .height(imageHeight.dp)
                .align(Alignment.Center)
                .scale(getScaleFactor(imageHeight))
        )
    }
}

fun getScaleFactor(imageHeight: Int): Float {
    val maxHeight = 500 // Define the maximum height you want to display the image
    return if (imageHeight > maxHeight) {
        maxHeight.toFloat() / imageHeight.toFloat()
    } else {
        1.0f
    }
}

@Composable

fun PopupDialog(onDialogClick: () -> Unit) {
    val viewModel: SorterViewModel = viewModel()

    if (viewModel.state.collectAsState().value.showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = "Sort Finished") },
            text = { Text(text = "Click OK to see the results") },
            confirmButton = {
                Button(onClick = onDialogClick)
//                    viewModel.updateShowDialog(false))
                {
                    Text(text = "OK")
                }
            }
        )
    }
}


@Composable
fun DummyScreen(
    imageA: Int = R.drawable.robocosan,
    imageB: Int = R.drawable.sakura_miko,
    nameA: String = "Tokino Sora",
    nameB: String = "AZKi",
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
//            modifier = Modifier.fillMaxSize()
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically

        ) {
            CardConstraint(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                image = imageA,
                name = nameA,
                onClick = onClick
            )
            CardConstraint(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                image = imageB,
                name = nameB,
                onClick = onClick
            )

        }
        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = onClick,
            modifier = Modifier.width(120.dp))
        {
            Text(text = "Draw")
        }
    }

}

@Composable
fun CardPair(
    imageA: Int,
    imageB: Int,
    nameA: String,
    nameB: String,
    matchNo: String,
    onClickA: () -> Unit,
    onClickB: () -> Unit,
    onClickButton: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Match #$matchNo",
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(60.dp))

        Spacer(modifier = Modifier.height(200.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CardConstraint(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                image = imageA,
                name = nameA,
                onClick = onClickA
            )
            CardConstraint(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                image = imageB,
                name = nameB,
                onClick = onClickB
            )

        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = onClickButton,
            modifier = Modifier.width(120.dp))
         {
            Text(text = "Draw")
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardConstraint(
    modifier: Modifier,
    image: Int,
    name: String,
    onClick: () -> Unit

) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()

    ) {
        val (imageRef, cardRef) = createRefs()

        Card(
            modifier = Modifier
                .constrainAs(cardRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .aspectRatio(0.8f)
                .padding(4.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(),
            onClick = onClick
        )
        {

            Text(
                text = name,
                modifier = Modifier
                    .vertical()
                    .rotate(90f),
            style = MaterialTheme.typography.titleLarge)

        }


        Image(
            painter = painterResource(image),
            contentDescription = "Image",
            modifier = Modifier
                .constrainAs(imageRef) {
                    start.linkTo(cardRef.start)
                    end.linkTo(cardRef.end)
                    top.linkTo(cardRef.top)
                    bottom.linkTo(cardRef.bottom)
                    height = Dimension.fillToConstraints
                }
                .scale(1.5f)

        )



    }
}



fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }

@Preview(showBackground = true)
@Composable
fun SorterScreenPreview() {
    ImageSorterTheme {
        DummyScreen()

    }
}