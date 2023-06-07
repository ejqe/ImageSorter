package com.ejqe.imagesorter.presentation

import com.ejqe.imagesorter.data.Player


data class ScreenState(

    val currentPair: Pair<Player, Player> = Player() to Player(), //
    val showDialog: Boolean = false,
    val matchNo: Int = 0,
    val progress: Float = 0f,
    val isClickable: Boolean = true,
    val isUndoClickable: Boolean = false,

    )



