package hu.bme.ait.blackjack.ui.screen.leaderboardscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import hu.bme.ait.blackjack.data.Player
import hu.bme.ait.blackjack.ui.screen.loginscreen.CasinoGreen
import hu.bme.ait.blackjack.ui.screen.loginscreen.DarkerGreen
import hu.bme.ait.blackjack.ui.screen.loginscreen.GoldAccent

@Composable
fun LeaderboardScreen() {
    val players = remember { mutableStateListOf<Player>() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance().collection("players")
            .orderBy("balance", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { result ->
                players.clear()
                for (document in result) {
                    val player = document.toObject(Player::class.java)
                    players.add(player)
                }
                isLoading = false
            }
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(CasinoGreen, DarkerGreen)
    )

    Column(
        modifier = Modifier.fillMaxSize().background(backgroundBrush).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "High Rollers",
            style = MaterialTheme.typography.displayMedium,
            color = GoldAccent,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(color = GoldAccent)
        } else {
            LazyColumn {
                itemsIndexed(players) { index, player ->
                    LeaderboardItem(rank = index + 1, player = player)
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(rank: Int, player: Player) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "#$rank",
                    color = GoldAccent,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.width(50.dp)
                )
                Text(
                    text = player.username,
                    color = Color.White,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }
            Text(
                text = "$${player.balance}",
                color = GoldAccent,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}