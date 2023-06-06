package com.ejqe.imagesorter.presentation

import com.ejqe.imagesorter.data.Player
import com.ejqe.imagesorter.data.PlayerUI

data class ScreenState(

    val currentPair: Pair<PlayerUI, PlayerUI> = PlayerUI() to PlayerUI(), //
    val showDialog: Boolean = false,
    val matchNo: Int = 0,
    val progress: Float = 0f,
    val isClickable: Boolean = true,
    val isUndoClickable: Boolean = false,

    )



