package hu.bme.ait.blackjack.ui.screen.loginscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

// Color constants matching StartScreen
val CasinoGreen = Color(0xFF35654d)
val DarkerGreen = Color(0xFF1E3A2B)
val GoldAccent = Color(0xFFFFD700)
val OffWhite = Color(0xFFF8F8F8)
val ButtonRed = Color(0xFF8C1818)

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("demo@ait.hu") }
    var password by rememberSaveable { mutableStateOf("123456") }

    val coroutineScope = rememberCoroutineScope()

    // Background Gradient
    val backgroundBrush = Brush.radialGradient(
        colors = listOf(CasinoGreen, DarkerGreen),
        center = Offset(x = 100f, y = 400f),
        radius = 1200f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
        // Decorative background elements
        CardSuitDecorations()

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Styled Title
            Text(
                text = "Player Entry",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 50.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = OffWhite,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(x = 4f, y = 4f),
                        blurRadius = 8f
                    )
                ),
                modifier = Modifier.padding(bottom = 30.dp)
            )

            // Styled Email Input
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                label = { Text(text = "E-mail") },
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Email, null, tint = GoldAccent)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldAccent,
                    unfocusedBorderColor = OffWhite.copy(alpha = 0.7f),
                    focusedLabelColor = GoldAccent,
                    unfocusedLabelColor = OffWhite.copy(alpha = 0.7f),
                    cursorColor = GoldAccent,
                    focusedTextColor = OffWhite,
                    unfocusedTextColor = OffWhite
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Styled Password Input
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                label = { Text(text = "Password") },
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(Icons.Default.Info, null, tint = GoldAccent)
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        if (showPassword) {
                            Icon(Icons.Default.Add, null, tint = GoldAccent)
                        } else {
                            Icon(Icons.Default.Clear, null, tint = GoldAccent)
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldAccent,
                    unfocusedBorderColor = OffWhite.copy(alpha = 0.7f),
                    focusedLabelColor = GoldAccent,
                    unfocusedLabelColor = OffWhite.copy(alpha = 0.7f),
                    cursorColor = GoldAccent,
                    focusedTextColor = OffWhite,
                    unfocusedTextColor = OffWhite
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Styled Buttons
            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Login Button
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val result = viewModel.loginUser(email, password)
                            if (result?.user != null) {
                                onLoginSuccess()
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonRed,
                        contentColor = GoldAccent
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Text(text = "LOGIN", fontWeight = FontWeight.Bold)
                }

                // Register Button
                Button(
                    onClick = {
                        viewModel.registerUser(email, password)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonRed,
                        contentColor = GoldAccent
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Text(text = "REGISTER", fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (viewModel.loginUiState) {
                is LoginUiState.Error -> {
                    Text(
                        text = "Error: ${(viewModel.loginUiState as LoginUiState.Error).errorMessage}",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
                is LoginUiState.Init -> {}
                is LoginUiState.Loading -> CircularProgressIndicator(color = GoldAccent)
                is LoginUiState.LoginSuccess -> Text("Login Successful", color = GoldAccent)
                is LoginUiState.RegisterSuccess -> Text("Registration Successful", color = GoldAccent)
            }
        }
    }
}

// Decoration composable reused from StartScreen
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