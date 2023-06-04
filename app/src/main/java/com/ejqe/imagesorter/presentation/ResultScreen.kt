package com.ejqe.imagesorter.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ejqe.imagesorter.R
import com.ejqe.imagesorter.presentation.ui.theme.ImageSorterTheme

@Composable
fun ResultScreen(viewModel: SorterViewModel) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        val players = viewModel.players.value.toList().sortedBy { it.rank }
        items(players) { player ->
/*            Card(
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
            }*/

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.6f)
                    .padding(8.dp)
            )
            {
                CardConstraint(
                    modifier = Modifier
                        .fillMaxWidth(),
                    image = player.image,
                    name = player.name,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = player.color,
                    onClick = {}
                )
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)

                        .shadow(4.dp, CircleShape),
                    shape = CircleShape


                ) {

                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = "#${player.rank}",
                        style = MaterialTheme.typography.titleMedium
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

        LazyVerticalGrid(columns = GridCells.Fixed(4))
        {
            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.6f)
                )
                {
                    CardConstraint(
                        modifier = Modifier.fillMaxWidth(),
                        image = R.drawable.houshou_marine,
                        name = "Houshou Marine",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = Color.Magenta,
                        onClick = {}
                    )
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .shadow(8.dp),
                        shape = CircleShape


                    ) {

                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = "#29"
                        )
                    }
                }


            }
        }
    }
}
