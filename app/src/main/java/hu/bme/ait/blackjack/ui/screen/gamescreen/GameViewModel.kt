package hu.bme.ait.blackjack.ui.screen.gamescreen

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.ait.blackjack.R
import hu.bme.ait.blackjack.data.Player
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

    data class Card (
        val suit: Suit,
        val rank: Rank,
        val imageRes: Int
    )

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = auth.currentUser

    private var deck: MutableList<Card> = mutableListOf()

    var canDoubleDown by mutableStateOf(false)
        private set

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
        startDataListener()
    }

    private fun startDataListener() {
        currentUser?.let { user ->
            firestore.collection("players").document(user.uid)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) return@addSnapshotListener
                    if (snapshot != null && snapshot.exists()) {
                        val player = snapshot.toObject(Player::class.java)
                        currentBalance = player?.balance ?: 0
                    }
                }
        }
    }

    private fun updateCloudBalance(newBalance: Int) {
        currentUser?.let { user ->
            firestore.collection("players").document(user.uid)
                .update("balance", newBalance)
        }
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
            updateCloudBalance(500)
        }
        playerHand.clear()
        dealerHand.clear()
        isGameOver = false
        gameResultMsg = ""
        currentBid = 0
        bidAmountBuffer = 0
        isBiddingPhase = true

        canDoubleDown = false
        isBiddingPhase = true
    }

    fun addChipToBuffer(chipValue: Int) {
        if (isBiddingPhase) {
            val potentialBid = bidAmountBuffer + chipValue
            if (potentialBid <= currentBalance) {
                bidAmountBuffer = potentialBid
            } else if (potentialBid > currentBalance){
                gameResultMsg = app.getString(R.string.insufficient_funds)
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

            canDoubleDown = currentBalance >= currentBid

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
            updateCloudBalance(currentBalance)
        } else {
            currentBid = 0
        }
    }

    fun hit(){
        if (isGameOver) return
        canDoubleDown = false
        playerHand.add(drawCard())

        if (isGameOver) return

        val score = calculatePoints(playerHand)

        if (score > 21) {
            checkBust()
            isGameOver = true
        }
    }

    fun stay(){
        if (isGameOver) return
        canDoubleDown = false
        while(calculatePoints(dealerHand) < 17) {
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
            gameResultMsg = app.getString(R.string.you_lose)
        }
    }

    fun checkWinner() {
        isGameOver = true
        val playerScore = calculatePoints(playerHand)
        val dealerScore = calculatePoints(dealerHand)

        when {
            dealerScore > 21 -> {
                gameResultMsg = app.getString(R.string.dealer_busts_you_win)
                currentBalance += currentBid * 2
            }
            playerScore > dealerScore -> {
                gameResultMsg = app.getString(R.string.you_win)
                currentBalance += currentBid * 2
            }
            playerScore < dealerScore -> {
                gameResultMsg = app.getString(R.string.dealer_wins)
            }
            else -> {
                gameResultMsg = app.getString(R.string.push_draw)
                currentBalance += currentBid
            }
        }

        updateCloudBalance(currentBalance)
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


    fun doubleDown() {
        if (!canDoubleDown || isGameOver || currentBalance < currentBid) return
        currentBalance -= currentBid
        currentBid *= 2
        playerHand.add(drawCard())
        canDoubleDown = false
        stay()
    }
}
