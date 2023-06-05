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
import kotlin.math.pow

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
    private var playersOp = _players.value.map { PlayerOp(name = it.name) }.toMutableList()


    init {
        _players.value.shuffle()
        generateMatches()
        updateCurPair("init")

    }

    fun onSelect(case: String) {

        backupPlayer()
        if (index == allMatches.size - 1) {//last item reached
            generateMatches()
            updateRatings(case)

            if (roundMatchSize == 0 || round > rounds) { //this ends the sorter
                calculateRank()
                _state.value = _state.value.copy(
                    isClickable = false, showDialog = true,
                    progress = 1f, matchNo = index + 2)
            } else { //2
                updateCurPair("next")
            }

        } else { //1
            updateRatings(case)
            updateCurPair("next")


        }
        _state.value = _state.value.copy(isUndoClickable = true)
    }

    fun onUndoClick() {
        _state.value = _state.value.copy(isUndoClickable = false)
        restorePlayer()
        updateCurPair("prev")

    }


    private fun generateMatches() {
        round++
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

        roundMatchSize = roundMatches.size

        println("Generated Matches: ${roundMatches.size}")
        roundMatches.forEach { println(it) }
        println("-------------------------")
    }

    fun updateShowDialog(value: Boolean) {
        _state.value = _state.value.copy(showDialog = value)
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

        val totalMatches = allMatches.size + roundMatchSize * (rounds - round + 1).toFloat()
        _state.value = _state.value.copy(
            progress = index / totalMatches,
            matchNo = index + 1)

    }

    private fun updateRatings(case: String) {
//        val (a, b) = findIndex(state.value.currentPair)
        val (playerA, playerB) = _state.value.currentPair

        val expectedScoreA = 1.0 / (1 + 10.0.pow((playerB.score - playerA.score) / 400))
        val expectedScoreB = 1.0 - expectedScoreA

        val kFactor = 40 // Adjust this value based on sensitivity




        when (case) {
            "Left" -> {
                playerA.score = playerA.score + kFactor * (1 - expectedScoreA)
                playerB.score = playerB.score + kFactor * (0 - expectedScoreB)
                playerA.wins += 1
                addOpData(
                    name = playerA.name,
                    defeated = playerB.name
                )
            }

            "Right" -> {
                playerA.score = playerA.score + kFactor * (0 - expectedScoreA)
                playerB.score = playerB.score + kFactor * (1 - expectedScoreB)
                playerB.wins += 1
                addOpData(
                    name = playerB.name,
                    defeated = playerA.name
                )
            }

            else -> {
                playerA.score = playerA.score + kFactor * (0.5 - expectedScoreA)
                playerB.score = playerB.score + kFactor * (0.5 - expectedScoreB)
                addOpData(
                    name = playerA.name,
                    draw = playerB.name
                )
                addOpData(
                    name = playerB.name,
                    draw = playerA.name
                )
            }
        }

        val p1 = state.value.currentPair.first
        val p2 = state.value.currentPair.second
        println("Name: ${p1.name}, Score: ${p1.score}, Wins: ${p1.wins}")
        println("Name: ${p2.name}, Score: ${p2.score}, Wins: ${p1.wins}")
        println("--------------------------------------")




    }


    private fun backupPlayer() {
        val pair = state.value.currentPair
        backup.player = pair

        val (a, b) = findIndex(pair)
        backup.playerOp = playersOp[a] to playersOp[b]


        println("BACKUP-A: ${backup.player.first.name} - ${backup.player.first.score}")
        println("BACKUP-B: ${backup.player.second.name} - ${backup.player.second.score}")
    }

    private fun restorePlayer() {
        val (indexA, indexB) = findIndex(backup.player)
        _players.value[indexA] = backup.player.first
        _players.value[indexB] = backup.player.second

        val pair = backup.playerOp
        val a = playersOp.indexOfFirst { it.name == pair.first.name }
        val b = playersOp.indexOfFirst { it.name == pair.second.name }
        playersOp[a] = backup.playerOp.first
        playersOp[b] = backup.playerOp.second



    }


    private fun findIndex(pair: Pair<Player, Player>): Pair<Int, Int> {
        val indexA = players.value.indexOfFirst { it.name == pair.first.name }
        val indexB = players.value.indexOfFirst { it.name == pair.second.name }
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


