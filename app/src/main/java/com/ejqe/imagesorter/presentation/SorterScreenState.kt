package com.ejqe.imagesorter.presentation

import com.ejqe.imagesorter.data.MasterList
import com.ejqe.imagesorter.data.Player

data class SorterScreenState(
    var players: List<Player>,
    val playedMatches: Set<Pair<Player, Player>>,
)
