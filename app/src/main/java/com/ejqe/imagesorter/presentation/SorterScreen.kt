package com.ejqe.imagesorter.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejqe.imagesorter.R
import com.ejqe.imagesorter.data.Player
import com.ejqe.imagesorter.ui.theme.ImageSorterTheme


@Composable
fun SorterScreen(
    onDialogClick: () -> Unit,
    viewModel: SorterViewModel,

    ) {

    val state = viewModel.state.collectAsState().value

    if (state.showDialog) {
        PopupDialog(onDialogClick = onDialogClick)
    }



    CardPair(
        playerA = viewModel.players.find { it.name == state.currentPair.first }!!,
        playerB = viewModel.players.find { it.name == state.currentPair.second }!!,
        matchNo = (viewModel.allMatches.indexOf(state.currentPair) + 1).toString(),
        progress = state.progress,
        onClickA = {
            if (viewModel.isClickable) {
                viewModel.onSelect(1)
            }
        },
        onClickB = {
            if (viewModel.isClickable) {
                viewModel.onSelect(2)
            }
        },
        onClickButton = {
            if (viewModel.isClickable) {
                viewModel.onSelect(3)
            }
        }
    )

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
fun CardPair(
    playerA: Player,
    playerB: Player,
    matchNo: String,
    progress: Float,
    onClickA: () -> Unit,
    onClickB: () -> Unit,
    onClickButton: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CardConstraint(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                image = playerA.image,
                name = playerA.name,
                color = playerA.color,
                onClick = onClickA
            )
            CardConstraint(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                image = playerB.image,
                name = playerB.name,
                color = playerB.color,
                onClick = onClickB
            )

        }

        Button(
            onClick = onClickButton,
            modifier = Modifier
                .width(120.dp)
                .bounceClick(0.8f, onClickButton)
        )
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
    color: Color,
    onClick: () -> Unit

) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()


    ) {
        val (imageRef, cardRef, textRef) = createRefs()

        Card(
            modifier = Modifier
                .constrainAs(cardRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .aspectRatio(0.7f)
                .padding(4.dp),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(
                containerColor = color
            ),
//            onClick = onClick,

        ){}


            Column(
                modifier = Modifier
                    .constrainAs(textRef) {
                        start.linkTo(cardRef.start)
                        top.linkTo(cardRef.top)
                    }
                    .padding(top = 5.dp)

            ){
                name.forEach { char ->
                Text(
                    text = char.toString(),
                    modifier = Modifier
                        .vertical()
                        .rotate(90f),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                }
            }

        Box(
            modifier = Modifier
                .constrainAs(imageRef) {
                    start.linkTo(cardRef.start)
                    end.linkTo(cardRef.end)
                    top.linkTo(cardRef.top)
                    bottom.linkTo(cardRef.bottom)
                    height = Dimension.fillToConstraints
                    width = Dimension.wrapContent
                }
//            .background(Color.Blue)
                .aspectRatio(1f)
                .scale(2f),
            contentAlignment = Alignment.Center
        ) {


            Image(
                painter = painterResource(image),
                contentDescription = "Image",
                modifier = Modifier
                    .bounceClick(0.8f, onClick)

            )
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
@Composable
fun DummyScreen(
) {
    val imageA: Int = R.drawable.houshou_marine
    val imageB: Int = R.drawable.sakura_miko
    val nameA = "Hoshimachi Suisei"
    val nameB = "AZKi"
    val matchNo = "8"
    val progress = 0.4f
    val colorA: Color = Color.Green
    val colorB: Color = Color.Red
    val onClick: () -> Unit = {}


    CardPair(
        playerA = Player(nameA, imageA, colorA),
        playerB = Player(nameB, imageB, colorB),
        matchNo = matchNo,
        progress = progress,

        onClickA = onClick,
        onClickB = onClick,
        onClickButton = onClick
    )


}

enum class ButtonState { Pressed, Idle }
fun Modifier.bounceClick(
    pressScale: Float,
    onClick: () -> Unit
) = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        targetValue = if (buttonState == ButtonState.Pressed) pressScale else 1f)
    val alpha by animateFloatAsState(
        targetValue = if (buttonState == ButtonState.Pressed) 0.7f else 1f)

    this
        .graphicsLayer {
            this.scaleX = scale
            this.scaleY = scale
            this.alpha
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}