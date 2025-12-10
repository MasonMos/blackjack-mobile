package hu.bme.ait.blackjack.ui.screen.gamescreen


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.ait.blackjack.ui.screen.startscreen.CasinoGreen
import hu.bme.ait.blackjack.ui.screen.startscreen.DarkerGreen
import hu.bme.ait.blackjack.ui.screen.startscreen.GoldAccent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

private val ChipRed = Color(0xFFB71C1C)
private val ChipBlue = Color(0xFF1565C0)
private val ChipWhite = Color(0xFFFFFFFF)
private val ChipBorder = Color(0xFF212121)
private val ChipShadow = Color(0x66000000)

@Composable
fun GameScreen() {
    val radialCenter = Offset(0.5f, 0.5f)
    val radialRadius = 0.9f


    val tableBrush = Brush.radialGradient(
        colors = listOf(CasinoGreen, DarkerGreen),
        center = radialCenter,
        radius = radialRadius,
        tileMode = TileMode.Clamp
    )

    val rimBrush = Brush.radialGradient(
        colors = listOf(GoldAccent, GoldAccent.copy(alpha = 0f)),
        center = radialCenter,
        radius = radialRadius * 1.05f,
        tileMode = TileMode.Clamp
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(tableBrush)
                drawRect(rimBrush)
            }
    ) {

        BetChip(
            value = "Chippy"
        )

        BetChip(
            value = "Chippy2",
            modifier = Modifier.align(Alignment.Center)
        )

        BetChip(
            value = "Hundo",
            backgroundColor = ChipBlue,
            sizeDp = 80.dp,                     // make it a little bigger for the centre
            onClick = {
            },
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun BetChip(
    value: String,
    sizeDp: Dp = 80.dp,
    backgroundColor: Color = ChipRed,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {

    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        animationSpec = tween(durationMillis = 120)
    )


    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(sizeDp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                // lift the chip a little when pressed (optional)
//                translationZ = if (pressed) 8f else 4f
//                translationZ = if (pressed) 8 else 4
            }
            .shadow(elevation = 8.dp, shape = CircleShape, ambientColor = ChipShadow, spotColor = ChipShadow)
            .clip(CircleShape)
            .background(color = backgroundColor, shape = CircleShape)
            .border(width = 2.dp, color = ChipBorder, shape = CircleShape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            pressed = true
                            // short haptic / visual feedback
                            onClick()
                        }
                    )
                } else Modifier
            )
            .pointerInput(Unit) {
                // Reset the pressed flag a few ms after a tap
                if (pressed) {
                    awaitPointerEventScope {
//                        delay(120)
                        pressed = false
                    }
                }
            }
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Outer radius = whole size
            val outerRadius = size.minDimension / 2f

            drawCircle(
                color = backgroundColor.copy(alpha = 0.85f),
                radius = outerRadius * 0.85f,
                center = center
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFD700).copy(alpha = 0.9f),
                        Color(0xFFFFD700).copy(alpha = 0f)
                    ),
                    radius = outerRadius
                ),
                radius = outerRadius,
                style = Stroke(width = outerRadius * 0.06f)
            )
        }

        Text(
            text = value,
            color = ChipWhite,
            style = TextStyle(
                fontSize = (sizeDp.value * 0.35f).sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

