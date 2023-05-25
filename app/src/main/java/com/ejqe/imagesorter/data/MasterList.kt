package com.ejqe.imagesorter.data

import com.ejqe.imagesorter.R


data class Player(
    val name: String,
    var score: Double,

    val image: Int = R.drawable.image,
    var rank: Int = 0
)

object MasterList {
    val players = mutableListOf(
        Player("ANNE", 1500.0),
        Player("BOBBY", 1500.0),
        Player("CINDY", 1500.0),
        Player("DONNA", 1500.0),
        Player("ERICK", 1500.0),
        Player("FATIMA", 1500.0),
        Player("GRACE", 1500.0),
        Player("HEART", 1500.0),
        Player("IVAN", 1500.0),
        Player("JOHN", 1500.0),
        Player("KEVIN", 1500.0),
        Player("LEAH", 1500.0),
        Player("MARIE", 1500.0),
        Player("NATHAN", 1500.0),
        Player("OLIVE", 1500.0),
        Player("PETER", 1500.0),
        Player("QUEEN", 1500.0),
        Player("RYAN", 1500.0),
        Player("SOFIA", 1500.0),
        Player("TOBY", 1500.0),
        Player("USHER", 1500.0),
        Player("VIVIAN", 1500.0),
        Player("WENDY", 1500.0),
        Player("XAVIER", 1500.0),
        Player("YVONNE", 1500.0),
        Player("ZANE", 1500.0),
        Player("ZANE2", 1500.0)
    )
}

