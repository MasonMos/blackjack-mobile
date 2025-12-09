package hu.bme.ait.blackjack.ui.screen.gamescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

class GameViewModel {
    enum class Suit {CLUBS, DIAMONDS, HEARTS, SPADES}
    enum class Rank(val value: Int)
    {
        TWO(2), THREE(3), FOUR(4), FIVE(5),
        SIX(6), SEVEN(7), EIGHT(8), NINE(9),
        TEN(10), JACK(10), QUEEN(10), KING(10),
        ACE(11)
    }

    enum class Chip(val value: Int)
    {
        ONE(1), FIVE(5), TEN(10), TWENTY_FIVE(25),
        FIFTY(50), ONE_HUNDRED(100), TWO_HUNDRED(200), FIVE_HUNDRED(500)
    }

    data class Card (
        val suit: Suit,
        val rank: Rank,
        //Image Resource
        //val imageRes: Int
    )

    private var deck: MutableList<Card> = mutableListOf()
    private var playerHand: MutableList<Card> = mutableListOf()
    private var dealerHand: MutableList<Card> = mutableListOf()

    var currentBalance by mutableStateOf(500)
        private set

    var currentBid by mutableStateOf(0)
        private set


    init{
        reset()
    }

    fun reset() {
        deck.clear()
        playerHand.clear()
        currentBid = 0
        for(suit in Suit.values()){
            for(rank in Rank.values()){
                deck.add(Card(suit, rank))
            }
        }
        shuffle()
    }
    fun shuffle(){
        deck.shuffle(Random.Default)
    }

    fun startHand(){
        hit(dealerHand)
        hit(dealerHand)
        hit(playerHand)
        hit(playerHand)
    }

    fun bid(amount : Int)
    {
        if (amount <= currentBalance)
        {
            currentBalance -= amount
            currentBid = amount
        }
        else
        {
            println("Insufficient funds")
        }
    }

    fun hit(player: MutableList<Card>){
        player.add(deck.removeAt(0))

        if(player == playerHand)
        {
            checkBust()
        }

    }

    fun stay(){
        while(dealerHand.sumOf { it.rank.value } < 17)
        {
            hit(dealerHand)
        }
        checkGameState()
    }

    fun checkBust(){
        if (playerHand.sumOf { it.rank.value } > 21){
            println("You Lose")
        }
    }

    fun checkGameState()
    {
        if (dealerHand.sumOf { it.rank.value } > 21) {
            println("You Win")
            currentBalance += currentBid * 2
        }
        else if(playerHand.sumOf { it.rank.value } == 21){
            println("You Win")
            currentBalance += currentBid * 2
        }
        else if(playerHand.sumOf { it.rank.value } > dealerHand.sumOf { it.rank.value }){
            println("You Win")
            currentBalance += currentBid * 2
        }
        else if (dealerHand.sumOf { it.rank.value } == 21) {
            println("You Lose")
        }
        else if (dealerHand.sumOf { it.rank.value } > playerHand.sumOf { it.rank.value }) {
            println("You Lose")
        }
        else if(playerHand.sumOf { it.rank.value } > 21){
            println("You Lose")
        }
        else if(playerHand.sumOf{it.rank.value} == dealerHand.sumOf{it.rank.value}){
            println("Draw")
            currentBalance += currentBid
        }
    }








    //Hit Function

    //Stay Function

    //Bid Function


}