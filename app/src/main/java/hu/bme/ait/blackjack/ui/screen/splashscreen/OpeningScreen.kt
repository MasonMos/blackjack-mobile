package hu.bme.ait.blackjack.ui.screen.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hu.bme.ait.blackjack.R
import kotlinx.coroutines.delay

@Composable
fun OpeningScreen(
    modifier: Modifier = Modifier,
    onTimeout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 40.dp, end = 40.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // You may add content here if needed
        }
        Image(
            painter = painterResource(
                R.drawable.bjlogo
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }

    LaunchedEffect(Unit) {
        delay(3000L)
        onTimeout()  // Notify parent composable that 3 seconds have passed
    }
}


