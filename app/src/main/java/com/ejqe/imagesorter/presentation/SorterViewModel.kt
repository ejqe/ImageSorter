package com.ejqe.imagesorter.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ejqe.imagesorter.data.MasterList
import com.ejqe.imagesorter.data.Player
import kotlin.math.ln
import kotlin.math.pow

class SorterViewModel(): ViewModel() {

    private val _state = mutableStateOf(
        SorterScreenState(players = listOf(), playedMatches = setOf()))
    val state: State<SorterScreenState> get() = _state

    private var players = MasterList.players.sortedByDescending { it.score }.toMutableList()
    private val playedMatches = mutableSetOf<Pair<Player, Player>>()

    init { runSorter() }


    private fun runSorter() {
        val rounds = (ln(MasterList.players.size.toDouble()) / ln(2.0)).toInt() + 1

        for (round in 1..rounds) {
            val matches = generateMatches()
            val totalMatches = playedMatches.size + matches.size*(rounds - round + 1)

            updateRatings(matches, totalMatches)
            players = players.sortedByDescending { it.score }.toMutableList()

        }

        //Display results here
        calculateRank()


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

    private fun generateMatches(): MutableList<Pair<Player, Player>> {

        val matches = mutableListOf<Pair<Player, Player>>()

        //Selecting Player1
        for (i in 0 until players.size - 1) {
            val playerA = players[i]
            //checks if player1 exist in any matches in this round, should only exist once
            if (matches.any { it.first == playerA || it.second == playerA }) {
                continue // Skip players who already have a match in this round
            }
            //Selecting Player2
            for (j in i + 1 until players.size) {
                val playerB = players[j]
                //checks if player2 exist in any matches in this round, should only exist once
                if (matches.any { it.first == playerB || it.second == playerB }) {
                    continue // Skip players who already have a match in this round
                }

                //checks if playedMatches contains this single match, if not, then add it to matches
                //if it exists, continue looping
                if (_state.value.playedMatches.none { (a, b) ->
                        (a.name == playerA.name && b.name == playerB.name) ||
                                (a.name == playerB.name && b.name == playerA.name)
                    }) {
                    matches.add(playerA to playerB)
                }
                break
            }
        }
        return matches
    }

    fun updateRatings(
        matches: MutableList<Pair<Player, Player>>,
        totalMatches: Int
    ) {

        for (match in matches) {
            val (playerA, playerB) = match

            val expectedScoreA = 1.0 / (1 + 10.0.pow((playerB.score - playerA.score) / 400))
            val expectedScoreB = 1.0 - expectedScoreA

            val kFactor = 40 // Adjust this value based on sensitivity

            playedMatches.add(playerA to playerB)
            val battle = playedMatches.indexOf(match) + 1
            val progress = (((battle-1).toDouble() / totalMatches.toDouble()) * 100).toInt()
            //Display Progress, and Views Here

            //WIN/LOSE Logic Here
            val case = 1

            val (scoreA, scoreB) = when (case) {
                1 -> playerA.score + kFactor * (1 - expectedScoreA) to
                        playerB.score + kFactor * (0 - expectedScoreB)

                2 -> playerA.score + kFactor * (0 - expectedScoreA) to
                        playerB.score + kFactor * (1 - expectedScoreB)

                else -> playerA.score + kFactor * (0.5 - expectedScoreA) to
                        playerB.score - kFactor * (0.5 - expectedScoreA)
            }

            val playerToUpdateA = players.find { it.name == playerA.name }
            playerToUpdateA?.score = scoreA
            val playerToUpdateB = players.find { it.name == playerB.name }
            playerToUpdateB?.score = scoreB

            //Display single match Result here
        }
    }




}