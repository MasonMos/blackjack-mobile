package hu.bme.ait.blackjack.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object StartScreenRoute: NavKey

data object GameScreenRoute: NavKey

data object LoginScreenRoute: NavKey

data object LeaderboardScreenRoute : NavKey
