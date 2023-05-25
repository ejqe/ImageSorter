package com.ejqe.imagesorter.presentation

import android.app.AlertDialog
import androidx.compose.runtime.mutableStateOf
import com.ejqe.imagesorter.data.MasterList
import com.ejqe.imagesorter.data.MasterList.players
import com.ejqe.imagesorter.data.Player

data class SorterScreenState(

    val currentPair: Pair<String, String> = ("" to ""),
    val showDialog: Boolean = false,
    val progress: Float = 0f

)
