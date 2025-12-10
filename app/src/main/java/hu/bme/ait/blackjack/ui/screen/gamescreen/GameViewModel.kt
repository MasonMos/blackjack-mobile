package hu.bme.ait.blackjack.ui.screen.gamescreen

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.ait.blackjack.R
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(
    private val app: Application
) : ViewModel() {
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
        FIFTY(50), ONE_HUNDRED(100)
    }

    data class Card (
        val suit: Suit,
        val rank: Rank,
        //Image Resource
        val imageRes: Int
    )

    private var deck: MutableList<Card> = mutableListOf()

    var playerHand = mutableStateListOf<Card>()
        private set

    var dealerHand = mutableStateListOf<Card>()
        private set

    var currentBalance by mutableStateOf(500)
        private set

    var currentBid by mutableStateOf(0)
        private set

    var bidAmountBuffer by mutableStateOf(0)
        private set

    var isBiddingPhase by mutableStateOf(true)
        private set

    var isGameOver by mutableStateOf(false)
        private set

    var gameResultMsg by mutableStateOf("")
        private set


    init{
        reset()
    }

    fun reset() {
        deck.clear()
        for(suit in Suit.values()){
            for(rank in Rank.values()){
                deck.add(Card(suit, rank, getCardImageResource(rank, suit)))
            }
        }
        shuffle()
    }

    fun shuffle(){
        deck.shuffle(Random.Default)
    }

    fun startNewGame(){
        if (deck.size < 10) {
            reset()
        }
        if (currentBalance <= 0) {
            currentBalance = 500
        }
        playerHand.clear()
        dealerHand.clear()
        isGameOver = false
        gameResultMsg = ""
        currentBid = 0
        bidAmountBuffer = 0
        isBiddingPhase = true
    }

    fun addChipToBuffer(chipValue: Int) {
        if (isBiddingPhase) {
            val potentialBid = bidAmountBuffer + chipValue
            if (potentialBid <= currentBalance) {
                bidAmountBuffer = potentialBid
            } else if (potentialBid > currentBalance){
                gameResultMsg = "Insufficient funds."
            }
            else {
                gameResultMsg = ""
            }
        }
    }

    fun resetBidBuffer() {
        bidAmountBuffer = 0
    }

    fun dealCards() {
        if (bidAmountBuffer > 0 && isBiddingPhase) {

            bid(bidAmountBuffer)

            isBiddingPhase = false

            dealerHand.add(drawCard())
            playerHand.add(drawCard())
            dealerHand.add(drawCard())
            playerHand.add(drawCard())
            checkNaturalBlackjack()
        }
    }

    fun drawCard(): Card {
        if (deck.isEmpty()) {
            reset()
        }
        return deck.removeAt(0)
    }

    fun bid(amount : Int) {
        if (amount <= currentBalance) {
            currentBalance -= amount
            currentBid = amount
        } else {
            currentBid = 0
        }
    }

    fun hit(){
        if (isGameOver) return
        playerHand.add(drawCard())

        val score = calculatePoints(playerHand)

        if (score > 21) {
            checkBust()
            isGameOver = true
        }
    }

    fun stay(){
        if (isGameOver) return
        while(calculatePoints(dealerHand) < 17)
        {
            dealerHand.add(drawCard())
        }
        checkWinner()
    }

    private fun checkNaturalBlackjack() {
        val playerScore = calculatePoints(playerHand)
        if (playerScore == 21) {

            stay()
        }
    }

    fun checkBust(){
        if (calculatePoints(playerHand) > 21){
            gameResultMsg = "You Lose"
        }
    }

    fun checkWinner() {
        isGameOver = true
        val playerScore = calculatePoints(playerHand)
        val dealerScore = calculatePoints(dealerHand)

        when {
            dealerScore > 21 -> {
                gameResultMsg = "Dealer Busts! You Win!"
                currentBalance += currentBid * 2
            }
            playerScore > dealerScore -> {
                gameResultMsg = "You Win!"
                currentBalance += currentBid * 2
            }
            playerScore < dealerScore -> {
                gameResultMsg = "Dealer Wins."
            }
            else -> {
                gameResultMsg = "Push (Draw)."
                currentBalance += currentBid
            }
        }
    }

    fun calculatePoints(hand: List<Card>): Int {
        var sum = 0
        var aceCount = 0

        for (card in hand) {
            sum += card.rank.value
            if (card.rank == Rank.ACE) {
                aceCount++
            }
        }

        while (sum > 21 && aceCount > 0) {
            sum -= 10
            aceCount--
        }

        return sum
    }

    private fun getCardImageResource(rank: Rank, suit: Suit): Int {
        val rankStr = when (rank) {
            Rank.ACE -> "ace"
            Rank.KING -> "king"
            Rank.QUEEN -> "queen"
            Rank.JACK -> "jack"
            else -> rank.value.toString()
        }
        val resourceName = "${suit.name.lowercase()}_${rankStr}"
        val resourceId = app.resources.getIdentifier(
            resourceName,
            "drawable",
            app.packageName
        )
        return if (resourceId != 0) resourceId else R.drawable.black_joker
    }
}
