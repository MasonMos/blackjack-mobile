package hu.bme.ait.blackjack

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.ait.blackjack.ui.navigation.GameScreenRoute
import hu.bme.ait.blackjack.ui.navigation.StartScreenRoute
import hu.bme.ait.blackjack.ui.screen.gamescreen.GameScreen
import hu.bme.ait.blackjack.ui.screen.startscreen.StartScreen
import hu.bme.ait.blackjack.ui.theme.BlackJackTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlackJackTheme {
                Scaffold{
                    NavGraph(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun NavGraph(modifier: Modifier) {
    val backStack = rememberNavBackStack(StartScreenRoute)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = {backStack.removeLastOrNull()},
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider  = entryProvider {
            entry<StartScreenRoute> {
                StartScreen(onStartClicked = {
                    backStack.add(GameScreenRoute)
                })
            }
            entry<GameScreenRoute> {
                GameScreen()
            }
        }
    )
}