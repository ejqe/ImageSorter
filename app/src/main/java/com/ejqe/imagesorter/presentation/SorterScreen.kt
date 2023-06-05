package com.ejqe.imagesorter.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejqe.imagesorter.R
import com.ejqe.imagesorter.data.Player
import com.ejqe.imagesorter.presentation.ui.components.CardOnImage
import com.ejqe.imagesorter.presentation.ui.components.ImageOnCard
import com.ejqe.imagesorter.presentation.ui.theme.ImageSorterTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SorterScreen(
    onDialogClick: () -> Unit,
    viewModel: SorterViewModel,
    ) {
    val state = viewModel.state.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Surface(shadowElevation = 3.dp) {
            TopAppBar(
                title = { Text(text = "Hololive Sorter", style = MaterialTheme.typography.titleLarge) },
            )
        }}
    ) {paddingValues ->

        if (state.showDialog) { PopupDialog(onDialogClick = onDialogClick) }

        ScreenContent(
            modifier = Modifier.padding(paddingValues),
            playerA = state.currentPair.first,
            playerB = state.currentPair.second,
            matchNo = state.matchNo.toString(),
            progress = state.progress,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            onClickA = {
                if (state.isClickable) {
                    viewModel.onSelect("Left")
                }
            },
            onClickB = {
                if (state.isClickable) {
                    viewModel.onSelect("Right")
                }
            },
            onDraw = {
                if (state.isClickable) {
                    viewModel.onSelect("Draw")
                }
            },
            onUndo = {
                if (state.isUndoClickable) {
                    viewModel.onUndoClick()


                }
            }
        )
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
                {
                    Text(text = "OK")
                }
            }
        )
    }
}




@Composable
fun ScreenContent(
    modifier: Modifier,
    playerA: Player,
    playerB: Player,
    matchNo: String,
    progress: Float,
    fontSize: TextUnit,
    onClickA: () -> Unit,
    onClickB: () -> Unit,
    onDraw: () -> Unit,
    onUndo: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Match #$matchNo",
            fontSize = 32.sp
        )

        LinearProgressIndicator(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            progress = progress
        )

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.9f)
        ) {

            val mid = createGuidelineFromStart(0.5f)
            val imBottom = createGuidelineFromBottom(0.1f)
            val (imageRefA, imageRefB,
                cardRefA, cardRefB)  = createRefs()


            CardOnImage(modifier = Modifier
                .constrainAs(cardRefA) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(mid)
                    width = Dimension.fillToConstraints
                }
                .padding(start = 8.dp, end = 4.dp),
                player = playerA,
                onClick = onClickA)

            CardOnImage(modifier = Modifier
                .constrainAs(cardRefB) {
                    start.linkTo(mid)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(start = 4.dp, end = 8.dp),
                player = playerB,
                onClick = onClickB)

            ImageOnCard(modifier = Modifier
                .constrainAs(imageRefA) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(imBottom)
                    end.linkTo(mid)
                    width = Dimension.fillToConstraints},
                player = playerA)

            ImageOnCard(modifier = Modifier
                .constrainAs(imageRefB) {
                    start.linkTo(mid)
                    top.linkTo(parent.top)
                    bottom.linkTo(imBottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints},
                player = playerB)

        }

        FilledTonalButton(
            onClick = onDraw,
            modifier = Modifier
                .width(150.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(10.dp)
        )
        {
            Text(text = "Draw",
            style = MaterialTheme.typography.titleMedium)
        }

        OutlinedButton(
            onClick = onUndo,
            modifier = Modifier
                .width(150.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(10.dp)
        )
        {
            Text(text = "Undo",
                style = MaterialTheme.typography.titleMedium)
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardConstraint(
    modifier: Modifier,
    image: Int,
    name: String,
    fontSize: TextUnit,
    color: Color,
    onClick: () -> Unit

) {

    Box(modifier = modifier.fillMaxWidth()) {


        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .height(350.dp)


        ) {
            val (imageRef, cardRef, textRef) = createRefs()

            Card(
                modifier = Modifier
                    .constrainAs(cardRef) {
                        start.linkTo(imageRef.start)
                        end.linkTo(imageRef.end)
                        top.linkTo(imageRef.top)
                        bottom.linkTo(imageRef.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .offset(y = 20.dp),
                shape = RoundedCornerShape(5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = color
                ),
                onClick = onClick,

                ) {}



            Box(
                modifier = Modifier
                    .constrainAs(imageRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                        width = Dimension.wrapContent
                    }
//                    .background(Color.Blue)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {


                Image(
                    painter = painterResource(image),
                    contentDescription = "Image",
                    modifier = Modifier

                )
            }


                    }
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DummyScreen(
) {
    val imageA: Int = R.drawable.error
    val imageB: Int = R.drawable.sakura_miko
    val nameA = "Hoshimachi Suisei"
    val nameB = "Tsunomaki Watame"
    val matchNo = "8"
    val progress = 0.4f
    val colorA: Color = Color.Green
    val colorB: Color = Color.Red
    val onClick: () -> Unit = {}


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Surface(shadowElevation = 3.dp) {

                TopAppBar(
                    title = {
                        Text(text = "Hololive Sorter",
                            style = MaterialTheme.typography.titleLarge)
                    },
                )
            }
        }

    ) { paddingValues ->

        ScreenContent(
            modifier = Modifier.padding(paddingValues),
            playerA = Player(nameA, imageA, colorA),
            playerB = Player(nameB, imageB, colorB),
            fontSize = 28.sp,
            matchNo = matchNo,
            progress = progress,

            onClickA = onClick,
            onClickB = onClick,
            onDraw = onClick,
            onUndo = onClick,
        )


    }
}
