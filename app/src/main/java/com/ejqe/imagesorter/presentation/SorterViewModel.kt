package com.ejqe.imagesorter.presentation

import androidx.lifecycle.ViewModel
import com.ejqe.imagesorter.data.Backup
import com.ejqe.imagesorter.data.MasterList
import com.ejqe.imagesorter.data.PlayerOp
import com.ejqe.imagesorter.data.Player
import com.ejqe.imagesorter.data.PlayerUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Random
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
        println("INIT")

    }

    fun onSelect(case: String) {

        backupPlayer()

        if (index == allMatches.size - 1) {//last item reached
            generateMatches()

            updateRatings(case)

            if (roundMatchSize == 0 || round > rounds) { //sorter end here (3)

                _state.value = _state.value.copy(
                    isClickable = false, showDialog = true,
                    progress = 1f, matchNo = index + 2)

                buchholz()
                _players.value.sortWith(
                    compareByDescending<Player> { it.score }.thenByDescending { it.tbScore })
                calculateRank()

                players.value.forEach { println(
                    "Rank: ${it.rank}, Score: ${it.score}, tbScore:${it.tbScore}, Name: ${it.name}") }
                println("----------------------------------------------")

                playersOp.forEach { println("Name: ${it.name}, \nDefeated: ${it.defeated}, \nDraw: ${it.draw}\n\n") }




            } else { //(2)
                updateCurPair("next")
            }

        } else { //(1)

            updateRatings(case)
            updateCurPair("next")

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
        val playerList = players.value.sortedWith(
            compareByDescending<Player> { it.score }.thenBy { Math.random()}).toMutableList()

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

        for ((index, player) in _players.value.withIndex()) {
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
        val a = players.value.first { it.name == pair.first }
        val b = players.value.first { it.name == pair.second }

        val playerA = PlayerUI(a.name, a.image, a.color)
        val playerB = PlayerUI(b.name, b.image, b.color)
        _state.value = _state.value.copy(currentPair = playerA to playerB)

        val totalMatches = allMatches.size + roundMatchSize * (rounds - round + 1).toFloat()
        _state.value = _state.value.copy(
            progress = index / totalMatches,
            matchNo = index + 1)

    }

    private fun updateRatings(case: String) {
        val (a, b) = findIndex(state.value.currentPair)

        val expectedScoreA = 1.0 / (1 + 10.0.pow((players.value[a].score - players.value[b].score) / 400))
        val expectedScoreB = 1.0 - expectedScoreA
        val kFactor = 40 // Adjust this value based on sensitivity

        when (case) {
            "Left" -> {
                _players.value[a].score += kFactor * (1 - expectedScoreA)
                _players.value[b].score += kFactor * (0 - expectedScoreB)

                addOpData(
                    name = players.value[a].name,
                    defeated = players.value[b].name
                )
            }

            "Right" -> {
                _players.value[a].score += kFactor * (0 - expectedScoreA)
                _players.value[b].score += kFactor * (1 - expectedScoreB)

                addOpData(
                    name = players.value[b].name,
                    defeated = players.value[a].name
                )
            }

            else -> {
                _players.value[a].score += kFactor * (0.5 - expectedScoreA)
                _players.value[b].score += kFactor * (0.5 - expectedScoreB)
                addOpData(name = players.value[a].name, draw = players.value[b].name)
                addOpData(name = players.value[b].name, draw =players.value[a].name)
            }
        }


//        val (a, b) = findIndex(state.value.currentPair)
//        val p1 = players.value[a]
//        val p2 = players.value[b]
//        println("Name: ${p1.name}, Score: ${p1.score}")
//        println("Name: ${p2.name}, Score: ${p2.score}")
//        println("--------------------------------------")







    }


    private fun backupPlayer() {
        val (aP, bP) = findIndex(state.value.currentPair)
        backup.player = players.value[aP].copy() to players.value[bP].copy()

        val (aOp, bOp) = findIndex(state.value.currentPair)
        backup.playerOp = playersOp[aOp] to playersOp[bOp]


//        println("BACKUP-A: ${backup.player.first.name} - ${backup.player.first.score}")
//        println("BACKUP-B: ${backup.player.second.name} - ${backup.player.second.score}")
    }

    private fun restorePlayer() {
        val pairP = backup.player
        val aP = players.value.indexOfFirst { it.name == pairP.first.name }
        val bP = players.value.indexOfFirst { it.name == pairP.second.name }
        _players.value[aP] = backup.player.first
        _players.value[bP] = backup.player.second

        val pairOp = backup.playerOp
        val aOp = playersOp.indexOfFirst { it.name == pairOp.first.name }
        val bOp = playersOp.indexOfFirst { it.name == pairOp.second.name }
        playersOp[aOp] = backup.playerOp.first
        playersOp[bOp] = backup.playerOp.second

    }


    private fun findIndex(pair: Pair<PlayerUI, PlayerUI>): Pair<Int, Int> {
        val indexA = players.value.indexOfFirst { it.name == pair.first.name }
        val indexB = players.value.indexOfFirst { it.name == pair.second.name }
        return indexA to indexB
    }

    private fun addOpData(name: String, defeated: String? = null, draw: String? = null) {
        val idx = playersOp.indexOfFirst { it.name == name }
        if (defeated != null) playersOp[idx].defeated.add(defeated)
        if (draw != null) playersOp[idx].draw.add(draw)
    }


    private fun buchholz() {

        for (player in playersOp) {

            val p = players.value.first { it.name == player.name }
            val defeatList = player.defeated.map { p.score }

            _players.value.first { it.name == player.name }.tbScore = defeatList.sum()
        }

    }
}



