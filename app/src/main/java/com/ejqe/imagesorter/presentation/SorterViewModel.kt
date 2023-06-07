package com.ejqe.imagesorter.presentation

import androidx.lifecycle.ViewModel
import com.ejqe.imagesorter.data.MasterList
import com.ejqe.imagesorter.data.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.pow


class SorterViewModel : ViewModel() {

    private val _state = MutableStateFlow(ScreenState())
    val state = _state.asStateFlow()

//    private val _players = MutableStateFlow(MasterList.players)
//    val players = _players.asStateFlow()

    val players = MasterList.players

    private var allMatches = mutableListOf<Pair<String, String>>()
    private var index: Int = 0

    private val rounds = ceil(ln(MasterList.players.size.toDouble()) / ln(2.0)).toInt()
    private var roundMatchSize = 0
    private var round = 0
    private var backup = Player() to Player()



    init {
        generateMatches()
        updateCurPair("init")
    }

    fun onSelect (case: String) {
        backupPlayer()
        updateRatings(case)
        if (round <= rounds) {
            if (index == allMatches.size - 1) generateMatches() //if last match for this round reached
            updateCurPair("next")
        } else { //sorter finished
            _state.value = _state.value.copy(
                isClickable = false, showDialog = true, progress = 1f)
            tieBreakScore()
            players.sortWith(compareByDescending<Player> { it.score }.thenByDescending { it.tbScore })
            calculateRank()

            players.forEach { println(
                "Rank: ${it.rank}, Score: ${it.score}, tbScore:${it.tbScore}, Defeaed:${it.defeated}  Name: ${it.name}") }
            println("----------------------------------------------")
        }
        _state.value = _state.value.copy(isUndoClickable = true)
    }

    fun onUndoClick() {
        _state.value = _state.value.copy(isUndoClickable = false)

        updateCurPair("prev")
        restorePlayer()

    }


    private fun generateMatches() {
        round++
        val roundMatches = mutableListOf<Pair<String, String>>()
        players.sortedWith(
            compareByDescending<Player> { it.score }.thenBy { Math.random()}).toMutableList()

        //Selecting Player1
        for (i in 0 until players.size - 1) {
            val nameA = players[i].name


            //checks if player1 exist in any matches in this round, should only exist once
            if (roundMatches.any { it.first == nameA || it.second == nameA }) {
                continue // Skip players who already have a match in this round
            }
            //Selecting Player2
            for (j in i + 1 until players.size) {
                val nameB = players[j].name


                //checks if player2 exist in any matches in this round, should only exist once
                if (roundMatches.any { it.first == nameB || it.second == nameB }) {
                    continue // Skip players who already have a match in this round
                }
                //checks if playedMatches contains this single match, if not, then add it to matches
                //if it exists, continue looping
                if (allMatches.none { (a, b) ->
                        (a == nameA && b == nameB) || (a == nameB && b == nameA) }) {
                    allMatches.add(nameA to nameB)
                    roundMatches.add(nameA to nameB)

                } else {
                    continue
                }
                break
            }
        }
        roundMatchSize = roundMatches.size

        println("-------------------------")
        println("Generated Matches: ${roundMatches.size}")
        roundMatches.forEach { println(it) }
        println("-------------------------")

        roundMatches.clear()
    }

    fun updateShowDialog(value: Boolean) {
        _state.value = _state.value.copy(showDialog = value)
    }



    private fun calculateRank() {
        var rank = 1
        var prevScore = Double.MAX_VALUE
        var prevTbScore = Double.MAX_VALUE

        for ((index, player) in players.withIndex()) {
            if (player.score < prevScore) {
                    rank = index + 1
            } else if (player.score == prevScore) {
                if (player.tbScore < prevTbScore) {
                    rank = index + 1
                }
            }
            player.rank = rank
            prevScore = player.score
            prevTbScore = player.tbScore
        }
    }



    private fun updateCurPair(case: String) {
        when (case) {
            "next" -> index++
            "prev" -> index--
            else -> {}
        }
        val pair = allMatches[index]
        val playerA = players.first { it.name == pair.first }
        val playerB = players.first { it.name == pair.second }

        _state.value = _state.value.copy(currentPair = playerA to playerB)

        val totalMatches = ((players.size/2) * rounds).toFloat()
        _state.value = _state.value.copy(
            progress = index / totalMatches,
            matchNo = index + 1)

    }

    private fun updateRatings(case: String) {
        val (a, b) = findIndex(state.value.currentPair)

        val expectedScoreA = 1.0 / (1 + 10.0.pow((players[a].score - players[b].score) / 400))
        val expectedScoreB = 1.0 - expectedScoreA
        val kFactor = 40 // Adjust this value based on sensitivity

        when (case) {
            "Left" -> {
                players[a].score += kFactor * (1 - expectedScoreA)
                players[b].score += kFactor * (0 - expectedScoreB)

                players[a].defeated += players[b].copy().name


            }

            "Right" -> {
                players[a].score += kFactor * (0 - expectedScoreA)
                players[b].score += kFactor * (1 - expectedScoreB)

                players[b].defeated += players[a].copy().name
            }

            else -> {
                players[a].score += kFactor * (0.5 - expectedScoreA)
                players[b].score += kFactor * (0.5 - expectedScoreB)

            }
        }


        val p1 = players[a]
        val p2 = players[b]
        println("RESULTS:: Name: ${p1.name}, Score: ${p1.score.toInt()}, Defeated: ${p1.defeated}")
        println("RESULTS:: Name: ${p2.name}, Score: ${p2.score.toInt()}, Defeated: ${p2.defeated}")
        println("--------------------------------------")

        println("backup after updateRatings A: ${backup.first.name} - ${backup.first.score.toInt()} - ${backup.first.defeated}")
        println("backup after updateRatings B: ${backup.second.name} - ${backup.second.score.toInt()} - ${backup.second.defeated}")



    }


    private fun backupPlayer() {
        val (a, b) = findIndex(state.value.currentPair)
        backup = players[a].copy(defeated = players[a].defeated.toMutableList()) to
                players[b].copy(defeated = players[b].defeated.toMutableList())

        println("BACKUP-A: ${backup.first.name} - ${backup.first.score.toInt()} - ${backup.first.defeated}")
        println("BACKUP-B: ${backup.second.name} - ${backup.second.score.toInt()} - ${backup.second.defeated}")
    }

    private fun restorePlayer() {
        val (a, b) = findIndex(backup)
        players[a] = backup.first
        players[b] = backup.second

        println("RESTORE-A: ${players[a].name} - ${players[a].score.toInt()} - ${players[a].defeated}")
        println("RESTORE-B: ${players[b].name} - ${players[b].score.toInt()} - ${players[b].defeated}")

    }


    private fun findIndex(pair: Pair<Player, Player>): Pair<Int, Int> {
        val indexA = players.indexOfFirst { it.name == pair.first.name }
        val indexB = players.indexOfFirst { it.name == pair.second.name }
        return indexA to indexB
    }


    private fun tieBreakScore() {

        for (item in players) {

            val player = players.first { it.name == item.name }
            val defeatList = item.defeated.map { player.score }
            player.tbScore = defeatList.sum()
        }
    }
}



