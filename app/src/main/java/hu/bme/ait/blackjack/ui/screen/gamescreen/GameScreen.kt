package hu.bme.ait.blackjack.ui.screen.gamescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.ait.blackjack.R // Ensure this matches your package
import hu.bme.ait.blackjack.ui.screen.startscreen.CasinoGreen
import hu.bme.ait.blackjack.ui.screen.startscreen.DarkerGreen

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel()
) {
    // Start game ONLY on first load
    LaunchedEffect(key1 = Unit) {
        gameViewModel.startNewGame()
    }

    val backgroundBrush = Brush.radialGradient(
        colors = listOf(CasinoGreen, DarkerGreen)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- Dealer Section ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Dealer",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                HandView(
                    cards = gameViewModel.dealerHand,
                    // Hide the first card if the game is NOT over yet
                    isHiddenCard = !gameViewModel.isGameOver
                )
                // Only show dealer score when game is over to avoid cheating!
                if (gameViewModel.isGameOver) {
                    Text(
                        text = "Score: ${gameViewModel.calculatePoints(gameViewModel.dealerHand)}",
                        color = Color.White
                    )
                }
            }

            // --- Game Info / Result ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Balance: $${gameViewModel.currentBalance}",
                    color = Color.Yellow,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Current Bid: $${gameViewModel.currentBid}",
                    color = Color.White,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Show Win/Loss Message
                if (gameViewModel.isGameOver) {
                    Text(
                        text = gameViewModel.gameResultMsg,
                        color = if (gameViewModel.gameResultMsg.contains("Win")) Color.Green else Color.Red,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // --- Player Section ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Player (Score: ${gameViewModel.calculatePoints(gameViewModel.playerHand)})",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                HandView(
                    cards = gameViewModel.playerHand,
                    isHiddenCard = false
                )
            }

            // --- Controls ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (!gameViewModel.isGameOver) {
                    Button(
                        onClick = { gameViewModel.hit() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                    ) {
                        Text("HIT")
                    }
                    Button(
                        onClick = { gameViewModel.stay() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("STAND")
                    }
                } else {
                    Button(
                        onClick = { gameViewModel.startNewGame() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF35654d))
                    ) {
                        Text("PLAY AGAIN")
                    }
                }
            }
        }
    }
}

@Composable
fun HandView(cards: List<GameViewModel.Card>, isHiddenCard: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp) // Fixed height to prevent jumping
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            cards.forEachIndexed { index, card ->
                // If it's the dealer's first card and hidden mode is on, show back of card
                val cardRes = if (isHiddenCard && index == 0) {
                    // Make sure you have a back_card drawable, or use a placeholder color
                    R.drawable.black_joker
                } else {
                    card.imageRes
                }

                Image(
                    painter = painterResource(id = cardRes),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(120.dp)
                        .width(80.dp)
                        .offset(x = (30 * index).dp)
                )
            }
        }
    }
}