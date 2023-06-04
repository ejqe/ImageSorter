package com.ejqe.imagesorter.presentation

import androidx.lifecycle.ViewModel
import com.ejqe.imagesorter.data.MasterList
import com.ejqe.imagesorter.data.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.ceil
import kotlin.math.ln

class SorterViewModel : ViewModel() {

    private val _state = MutableStateFlow(SorterScreenState())
    val state = _state.asStateFlow()

    private val _players = MutableStateFlow(MasterList.players)
    val players = _players.asStateFlow()

//    private var playerList = players.value.sortedByDescending { it.score }.toMutableList()
    private var allMatches = mutableListOf<Pair<String, String>>()
    private var index: Int = 0

    private var roundMatchSize = 0
    private var round = 0
    private var prevScores = 0.0 to 0.0
    private val rounds = ceil(ln(MasterList.players.size.toDouble()) / ln(2.0)).toInt()




    init {

        generateMatches()

        updateCurPair()

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
                        (a == nameA && b == nameB) ||
                                (a == nameB && b == nameA)
                    }) {
                    roundMatches.add(nameA to nameB)
                    allMatches.add(nameA to nameB)

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
        val currentMatches = index + 1f
        val totalMatches = allMatches.size + roundMatchSize * (rounds - round + 1).toFloat()
        _state.value = _state.value.copy(
            progress = currentMatches / totalMatches,
            matchNo = (currentMatches + 1f).toInt().toString())
    }

    private fun calculateRank() {
        var rank = 1
        var previousScore = Double.MAX_VALUE

        for( (index, player) in _players.value.withIndex() ) {
            if (player.score < previousScore) {
                rank = index +1
            }
            player.rank = rank
            previousScore = player.score
        }
    }

    private fun updateCurPair() {

        val stringPair = allMatches[index]
        val playerA = _players.value.find { it.name == stringPair.first }!!
        val playerB = _players.value.find { it.name == stringPair.second }!!

        _state.value = _state.value.copy(currentPair = playerA to playerB)

    }
    fun onSelect(case: Int) {

        calculateProgress()

        if (index < allMatches.size - 1) {
            updateRatings(case)
            index++
            updateCurPair()

        } else {
            updateRatings(case)
            generateMatches()

            if (roundMatchSize == 0 || round == rounds + 1) { //this ends the sorter
                calculateRank()

                _state.value = _state.value.copy(isClickable = false, progress = 1f, showDialog = true)
            } else {
               index++
               updateCurPair()
            }
        }
        _state.value = _state.value.copy(isUndoClickable = true)
    }

    private fun updateRatings(case: Int) {
        prevScores = when (case) {
            1 -> 1.0 to 0.0
            2 -> 0.0 to 1.0
            else -> 0.5 to 0.5 }
        uploadScore(1)
    }

    fun onUndoClick() {
        index--
        updateCurPair()
        _state.value = _state.value.copy(isUndoClickable = false)
        uploadScore(-1)
    }

    private fun uploadScore(plusMinus: Int) {
        val stringPair = allMatches[index]
        val playerA = _players.value.find { it.name == stringPair.first }
        val playerB = _players.value.find { it.name == stringPair.second }

        playerA?.score = playerA?.score!! + prevScores.first * plusMinus
        playerB?.score = playerB?.score!! + prevScores.second * plusMinus




        //update to playerList

    }

    fun tieBreaker() {
        //summing the conventional score of each defeated opponent
        //half the conventional score of each drawn opponent

        //used before calculateRank()
    }

    fun byePlayer() {
        //select last player that is not on the list, use ascending list and for loop
        //add 1.0 to the existing score
    }

}