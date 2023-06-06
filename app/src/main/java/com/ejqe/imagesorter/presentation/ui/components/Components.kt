package com.ejqe.imagesorter.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ejqe.imagesorter.data.Player
import com.ejqe.imagesorter.data.PlayerUI

@Composable
fun ImageOnCard(modifier: Modifier, player: PlayerUI) {
    Box(modifier = modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .padding(bottom = 50.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = player.image),
            contentDescription = "Image",
            modifier = Modifier
                .scale(2.8f)
                .background(Color(0x42ffde17))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardOnImage(modifier: Modifier, player: PlayerUI, onClick: () -> Unit) {
    Card(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(top = 24.dp),
        shape = RoundedCornerShape(10),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onClick
    ) {

        Box(modifier = Modifier
            .fillMaxSize()
            .background(player.color)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.BottomCenter),

                colors = CardDefaults.cardColors(Color(0x7F000000))
            ) {

                Text(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterHorizontally),
                    text = player.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
            }

        }

    }

}
