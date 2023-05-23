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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejqe.imagesorter.R
import com.ejqe.imagesorter.data.MasterList
import com.ejqe.imagesorter.ui.theme.ImageSorterTheme

@Composable
fun SorterScreen() {

    val players = MasterList.players
    val playerA = players[0]
    val playerB = players[1]

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Match #1", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(60.dp))

        LinearProgressIndicator(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            progress = 0.8f)

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {

            CardItem(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                text = playerA.name
            )
            CardItem(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                text = playerB.name
            )
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .width(120.dp)
                .padding(8.dp)
            ,
            ) {

            Text(text = "Draw")
        }

    }
}


@Composable
fun CardItem(modifier: Modifier, text: String) {
    Card(modifier = modifier) {
        Column {
            Image(
                painter = painterResource(R.drawable.image),
                contentDescription = "Image"
            )

            Text(
                text = text,
                modifier = Modifier.padding(
                    horizontal = 8.dp, vertical = 4.dp
                )

            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SorterScreenPreview() {
    ImageSorterTheme {
        SorterScreen()
    }
}