package com.ejqe.imagesorter.presentation

import androidx.lifecycle.ViewModel
import com.ejqe.imagesorter.data.Backup
import com.ejqe.imagesorter.data.MasterList
import com.ejqe.imagesorter.data.PlayerOp
import com.ejqe.imagesorter.data.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.ceil
import kotlin.math.ln

class SorterViewModel : ViewModel() {

    private val _state = MutableStateFlow(ScreenState())
    val state = _state.asStateFlow()

    private val _players = MutableStateFlow(MasterList.players)
    val players = _players.asStateFlow()

    private var allMatches = mutableListOf<Pair<String, String>>()
    private var index: Int = 0

    private val rounds = ceil(ln(MasterList.players.size.toDouble()) / ln(2.0)).toInt()
    private var roundMatchSize = 0
    private var round = 0
    private var backup = Backup()
    private var backupP = Player() to Player()
    private var backupO = PlayerOp() to PlayerOp()

    private var byeList = mutableListOf<String>()
    private var playersOp = _players.value.map { PlayerOp(name = it.name) }.toMutableList()


    init {
        _players.value.shuffle()
        generateMatches()
        updateCurPair("init")
    }


    private fun generateMatches() {

        val roundMatches = mutableListOf<Pair<String, String>>()
        val playerList = players.value.sortedByDescending { it.score }.toMutableList()

        //Selecting Player1
        for (i in 0 until playerList.size - 1) {
            val nameA = playerList[i].name
            //checks if player1 exist in any matches in this round, should only exist once
            if (roundMatches.any { it.first == nameA || it.second == nameA }) {
                continue // Skip players who already have a match in this round
            }
            //Selecting Player2
            for (j in i + 1 until playerList.size) {
                val nameB = playerList[j].name
                //checks if player2 exist in any matches in this round, should only exist once
                if (roundMatches.any { it.first == nameB || it.second == nameB }) {
                    continue // Skip players who already have a match in this round
                }
                //checks if playedMatches contains this single match, if not, then add it to matches
                //if it exists, continue looping
                if (allMatches.none { (a, b) ->
                        (a == nameA && b == nameB) || (a == nameB && b == nameA)
                    })

                    roundMatches.add(nameA to nameB)
//                allMatches.add(nameA to nameB)

                break
            }
        }

        allMatches += roundMatches
        promoteBye(playerList)

        round++
        roundMatchSize = roundMatches.size
    }

    private fun promoteBye(playerList: MutableList<Player>) {
        if (playerList.size % 2 == 1) {
            for (i in playerList.size - 1 downTo 0) {
                val name = playerList[0].name
                if (!byeList.contains(name)) {
                    byeList.add(name)
                    val promoted = _players.value.find { it.name == name }!!
                    promoted.score = promoted.score + 1.0

                    break
                }
            }
        }
    }

    fun updateShowDialog(value: Boolean) {
        _state.value = _state.value.copy(showDialog = value)
    }

    private fun calculateProgress() {
        //estimate total matches
        val totalMatches = allMatches.size + roundMatchSize * (rounds - round + 1).toFloat()
        _state.value = _state.value.copy(
            progress = (index + 1) / totalMatches,
            matchNo = index + 2
        )
    }

    private fun calculateRank() {
        var rank = 1
        var previousScore = Double.MAX_VALUE

        for ((index, player) in _players.value.withIndex()) {
            if (player.score < previousScore) {
                rank = index + 1
            }
            player.rank = rank
            previousScore = player.score
        }
    }


    fun onSelect(case: String) {
        calculateProgress()
        backupPlayer()
        if (index != allMatches.size - 1) {

            updateRatings(case)
            updateCurPair("next")

        } else { //next round

            updateRatings(case)
            generateMatches()

            if (roundMatchSize == 0 || round > rounds) { //this ends the sorter
                calculateRank()
                _state.value = _state.value.copy(
                    isClickable = false, progress = 1f, showDialog = true
                )
            }
            updateCurPair("next")

        }
        _state.value = _state.value.copy(isUndoClickable = true)
    }

    fun onUndoClick() {
        _state.value = _state.value.copy(isUndoClickable = false, matchNo = index)
        restorePlayer()
        updateCurPair("prev")
    }

    private fun updateCurPair(case: String) {
        when (case) {
            "next" -> index++
            "prev" -> index--
            else -> {}
        }
        val pair = allMatches[index]
        val playerA = players.value.first { it.name == pair.first }
        val playerB = players.value.first { it.name == pair.second }

        _state.value = _state.value.copy(currentPair = playerA to playerB)
    }

    private fun updateRatings(case: String) {
        val (a, b) = findIndex(state.value.currentPair)

        when (case) {
            "Left" -> {
                _players.value[a].score += 1.0
                _players.value[b].score += 0.0
                _players.value[a].wins += 1
                addOpData(
                    name = players.value[a].name,
                    defeated = players.value[b].name,
                    draw = ""
                )
            }

            "Right" -> {
                _players.value[a].score += 0.0
                _players.value[b].score += 1.0
                _players.value[b].wins += 1
                addOpData(
                    name = players.value[b].name,
                    defeated = players.value[a].name
                )
            }

            else -> {
                _players.value[a].score += 0.5
                _players.value[b].score += 0.5
                addOpData(
                    name = players.value[a].name,
                    draw = players.value[b].name
                )
                addOpData(
                    name = players.value[b].name,
                    draw = players.value[a].name
                )
            }
        }
    }


    private fun backupPlayer() {
        val pair = state.value.currentPair
        backup.player = pair

        val (a, b) = findIndex(pair)
        backup.playerOp = playersOp[a] to playersOp[b]


    }

    private fun restorePlayer() {
        val (indexA, indexB) = findIndex(backup.player)
        _players.value[indexA] = backup.player.first
        _players.value[indexB] = backup.player.second

        val pair = backup.playerOp
        val (a, b) = findIndexOp(pair)
        playersOp[a] = backup.playerOp.first
        playersOp[b] = backup.playerOp.second



    }


    private fun findIndex(pair: Pair<Player, Player>): Pair<Int, Int> {
        val indexA = players.value.indexOfFirst { it.name == pair.first.name }
        val indexB = players.value.indexOfFirst { it.name == pair.second.name }
        return indexA to indexB
    }

    private fun findIndexOp(pair: Pair<PlayerOp, PlayerOp>): Pair<Int, Int> {
        val indexA = playersOp.indexOfFirst { it.name == pair.first.name }
        val indexB = playersOp.indexOfFirst { it.name == pair.second.name }
        return indexA to indexB
    }


    private fun addOpData(name: String, defeated: String = "", draw: String = "") {
        val idx = playersOp.indexOfFirst { it.name == name }
        playersOp[idx].defeated.add(defeated)
        playersOp[idx].draw.add(draw)
    }



    //summing the conventional score of each defeated opponent
    //half the conventional score of each drawn opponent

    //used before calculateRank()
}


//select last player that is not on the list, use ascending list and for loop
//add 1.0 to the existing score


