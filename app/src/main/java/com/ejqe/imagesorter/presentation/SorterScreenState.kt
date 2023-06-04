package com.ejqe.imagesorter.presentation

import com.ejqe.imagesorter.data.Player

data class SorterScreenState(

    val currentPair: Pair<Player, Player> = Player() to Player(),
    val showDialog: Boolean = false,
    val matchNo: String = "1",
    val progress: Float = 0f,
    val isClickable: Boolean = true,
    val isUndoClickable: Boolean = false,

    )
