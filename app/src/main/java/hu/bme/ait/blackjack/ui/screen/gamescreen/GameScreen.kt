package hu.bme.ait.blackjack.ui.screen.gamescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.ait.blackjack.R
import hu.bme.ait.blackjack.ui.screen.startscreen.CasinoGreen
import hu.bme.ait.blackjack.ui.screen.startscreen.DarkerGreen

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel()
) {

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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.dealer),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                HandView(
                    cards = gameViewModel.dealerHand,
                    isHiddenCard = !gameViewModel.isGameOver && !gameViewModel.isBiddingPhase
                )
                if (gameViewModel.isGameOver) {
                    Text(
                        text = stringResource(id = R.string.score, gameViewModel.calculatePoints(gameViewModel.dealerHand)),
                        color = Color.White
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(id = R.string.balance, gameViewModel.currentBalance),
                    color = Color.Yellow,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                if (gameViewModel.isBiddingPhase) {
                    BiddingControls(gameViewModel)
                } else {
                    GamePhaseInfo(gameViewModel)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(id = R.string.player_score, gameViewModel.calculatePoints(gameViewModel.playerHand)),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                HandView(
                    cards = gameViewModel.playerHand,
                    isHiddenCard = false
                )
            }
            if (!gameViewModel.isBiddingPhase) {
                GameActionButtons(gameViewModel)
            }
        }
    }
}

@Composable
fun HandView(cards: List<GameViewModel.Card>, isHiddenCard: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            cards.forEachIndexed { index, card ->
                val cardRes = if (isHiddenCard && index == 0) {
                    R.drawable.card_back_red
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

@Composable
fun ChipButton(value: Int, Image: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = Image),
        contentDescription = stringResource(id = R.string.chip_content_description, value),
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(55.dp)
            .clickable(onClick = onClick)
    )
}

@Composable
fun GameActionButtons(gameViewModel: GameViewModel) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (!gameViewModel.isGameOver) {
            Button(
                onClick = { gameViewModel.hit() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(stringResource(R.string.hit))
            }

            Button(
                onClick = { gameViewModel.doubleDown() },
                enabled = gameViewModel.canDoubleDown,
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text("DOUBLE DOWN")
            }

            Button(
                onClick = { gameViewModel.stay() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(stringResource(R.string.stand))
            }
        } else {
            Button(
                onClick = { gameViewModel.startNewGame() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF35654d))
            ) {
                Text(stringResource(R.string.new_round))
            }
        }
    }
}



@Composable
fun GamePhaseInfo(gameViewModel: GameViewModel) {
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = stringResource(id = R.string.current_bid, gameViewModel.currentBid),
        color = Color.White,
        fontSize = 16.sp
    )

    if (gameViewModel.isGameOver) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = gameViewModel.gameResultMsg,
            color = if (gameViewModel.gameResultMsg.contains(stringResource(R.string.you_win))) Color.Green else Color.Red,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun BiddingControls(gameViewModel: GameViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.bet_amount, gameViewModel.bidAmountBuffer),
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ChipButton(value = 1, Image = R.drawable.chip1, onClick = { gameViewModel.addChipToBuffer(1) })
            ChipButton(value = 5, Image = R.drawable.chip5, onClick = { gameViewModel.addChipToBuffer(5) })
            ChipButton(value = 25, Image = R.drawable.chip25, onClick = { gameViewModel.addChipToBuffer(25) })
            ChipButton(value = 100, Image = R.drawable.chip100, onClick = { gameViewModel.addChipToBuffer(100) })
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = { gameViewModel.dealCards() },
                enabled = gameViewModel.bidAmountBuffer > 0,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(stringResource(R.string.deal))
            }
            Spacer(modifier = Modifier.width(15.dp))
            Button(
                onClick = { gameViewModel.resetBidBuffer() },
                enabled = gameViewModel.bidAmountBuffer > 0,
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
                Text(stringResource(R.string.clear))
            }
        }
    }
}
