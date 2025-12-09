package hu.bme.ait.blackjack.ui.screen.startscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.ait.blackjack.ui.theme.BlackJackTheme // Make sure you have a theme file

// Define some thematic colors for reuse
val CasinoGreen = Color(0xFF35654d)
val DarkerGreen = Color(0xFF1E3A2B)
val GoldAccent = Color(0xFFFFD700)
val OffWhite = Color(0xFFF8F8F8)
val ButtonRed = Color(0xFF8C1818)


@Composable
fun StartScreen(
    onStartClicked: () -> Unit = {},
    onStatClicked: () -> Unit = {}
) {
    // A radial gradient gives the background the appearance of a lit table
    val backgroundBrush = Brush.radialGradient(
        colors = listOf(CasinoGreen, DarkerGreen),
        center = Offset(x = 100f, y = 400f),
        radius = 1200f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        // Subtle card suit icons in the corners for decoration
        CardSuitDecorations()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title Text with a shadow for a premium feel
            Text(
                text = "Blackjack",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 70.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = OffWhite,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(x = 4f, y = 4f),
                        blurRadius = 8f
                    )
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Start Game Button
            Button(
                onClick = onStartClicked,
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonRed,
                    contentColor = GoldAccent
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Text(
                    text = "START GAME",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = onStatClicked,
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonRed,
                    contentColor = GoldAccent
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Text(
                    text = "VIEW STATS",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun CardSuitDecorations() {
    val suitColor = Color.Black.copy(alpha = 0.2f)
    val suitSize = 50.sp
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("♠", fontSize = suitSize, color = suitColor, modifier = Modifier.align(Alignment.TopStart))
        Text("♥", fontSize = suitSize, color = suitColor, modifier = Modifier.align(Alignment.TopEnd))
        Text("♣", fontSize = suitSize, color = suitColor, modifier = Modifier.align(Alignment.BottomStart))
        Text("♦", fontSize = suitSize, color = suitColor, modifier = Modifier.align(Alignment.BottomEnd))
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    BlackJackTheme {
        StartScreen()
    }
}