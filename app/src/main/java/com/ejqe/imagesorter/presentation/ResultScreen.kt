package com.ejqe.imagesorter.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ejqe.imagesorter.R
import com.ejqe.imagesorter.ui.theme.ImageSorterTheme

@Composable
fun ResultScreen(viewModel: SorterViewModel) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)
    ) {
        val players = viewModel.players.toList()
        items(players) {player ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .height(250.dp)
                    .aspectRatio(0.6f),
                shape = RoundedCornerShape(10),

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
                        text = player.name,
                        modifier = Modifier.padding(
                            horizontal = 8.dp, vertical = 4.dp
                        )
                    )

                    Text(
                        text = "#${player.rank} - ${player.score.toInt()}",
                        modifier = Modifier.padding(
                            horizontal = 8.dp, vertical = 4.dp
                        )
                    )
                }
            }
        }

    }
}



@Preview(showBackground = true)
@Composable
fun ItemPreview() {
    ImageSorterTheme {

    }
}