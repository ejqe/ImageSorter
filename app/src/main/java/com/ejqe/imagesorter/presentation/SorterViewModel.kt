package com.ejqe.imagesorter.presentation

import androidx.lifecycle.ViewModel
import com.ejqe.imagesorter.data.MasterList
import com.ejqe.imagesorter.data.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.ln
import kotlin.math.pow

class SorterViewModel : ViewModel() {

    private val _state = MutableStateFlow(SorterScreenState())
    val state = _state.asStateFlow()

    var isClickable = true

    var players: MutableList<Player> =
        MasterList.players.sortedByDescending { it.score }.toMutableList()
    var allMatches = mutableListOf<Pair<String, String>>()

    private var index: Int = 0
    private var roundMatchSize = 0
    private var round = 0
    private val rounds = (ln(MasterList.players.size.toDouble()) / ln(2.0)).toInt() + 1

    init {
        generateMatches()
        _state.value = _state.value.copy(currentPair = allMatches[index])
    }



    private fun generateMatches() {

        val roundMatches = mutableListOf<Pair<String, String>>()
        players = players.sortedByDescending { it.score }.toMutableList()

        //Selecting Player1
        for (i in 0 until players.size - 1) {
            val playerNameA = players[i].name
            //checks if player1 exist in any matches in this round, should only exist once
            if (roundMatches.any { it.first == playerNameA || it.second == playerNameA }) {
                continue // Skip players who already have a match in this round
            }
            //Selecting Player2
            for (j in i + 1 until players.size) {
                val playerNameB = players[j].name
                //checks if player2 exist in any matches in this round, should only exist once
                if (roundMatches.any { it.first == playerNameB || it.second == playerNameB }) {
                    continue // Skip players who already have a match in this round
                }
                //checks if playedMatches contains this single match, if not, then add it to matches
                //if it exists, continue looping
                if (allMatches.none { (a, b) ->
                        (a == playerNameA && b == playerNameB) ||
                                (a == playerNameB && b == playerNameA)
                    }) {
                    roundMatches.add(playerNameA to playerNameB)
                    allMatches.add(playerNameA to playerNameB)
                }
                break
            }
        }
        round++
        roundMatchSize = roundMatches.size
    }

    fun updateShowDialog(value: Boolean) {
        _state.value = _state.value.copy(showDialog = value)
    }

    private fun calculateProgress() {
        val currentMatches = allMatches.indexOf(state.value.currentPair).toFloat()
        val totalMatches = allMatches.size + roundMatchSize * (rounds - round + 1).toFloat()
        _state.value = _state.value.copy(progress = currentMatches / totalMatches)
    }

    private fun calculateRank() {
        var rank = 1
        var previousScore = Double.MAX_VALUE

        for( (index, player) in players.withIndex() ) {
            if (player.score < previousScore) {
                rank = index +1
            }
            player.rank = rank
            previousScore = player.score
        }
    }
    fun onSelect(case: Int) {

        calculateProgress()

        if (index < allMatches.size - 1) {
            updateRatings(case)
            index++
            _state.value = _state.value.copy(currentPair = allMatches[index])
        } else {
            updateRatings(case)
            generateMatches()
            if (roundMatchSize == 0 || round == rounds + 1) {  //this ends the sorter
                isClickable = false
                _state.value = _state.value.copy(progress = 1f)
                calculateRank()
                _state.value = _state.value.copy(showDialog = true)
            } else {
                index++
                _state.value = _state.value.copy(currentPair = allMatches[index])
            }
        }

    }


    private fun updateRatings(case: Int) {

        val (playerNameA, playerNameB) = allMatches[index]

        //Get instance of Player pair
        val playerA = players.find { it.name == playerNameA }!!
        val playerB = players.find { it.name == playerNameB }!!

        val expectedScoreA = 1.0 / (1 + 10.0.pow((playerB.score - playerA.score) / 400))
        val expectedScoreB = 1.0 - expectedScoreA
        val kFactor = 40 // Adjust this value based on sensitivity

        val (scoreA, scoreB) = when (case) {
            1 -> playerA.score + kFactor * (1 - expectedScoreA) to
                    playerB.score + kFactor * (0 - expectedScoreB)

            2 -> playerA.score + kFactor * (0 - expectedScoreA) to
                    playerB.score + kFactor * (1 - expectedScoreB)

            else -> playerA.score + kFactor * (0.5 - expectedScoreA) to
                    playerB.score - kFactor * (0.5 - expectedScoreA)
        }
        //Update Score in players state
        playerA.score = scoreA
        playerB.score = scoreB

    }


}