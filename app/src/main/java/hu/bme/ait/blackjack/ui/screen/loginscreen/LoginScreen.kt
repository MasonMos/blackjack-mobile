package hu.bme.ait.blackjack.ui.screen.loginscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.ait.blackjack.R
import kotlinx.coroutines.launch

// Color constants matching StartScreen
val CasinoGreen = Color(0xFF35654d)
val DarkerGreen = Color(0xFF1E3A2B)
val GoldAccent = Color(0xFFFFD700)
val OffWhite = Color(0xFFF8F8F8)
val ButtonRed = Color(0xFF8C1818)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("demo@ait.hu") }
    var password by rememberSaveable { mutableStateOf("123456") }

    var username by rememberSaveable { mutableStateOf("") }
    var isRegisterMode by rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

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
        CardSuitDecorations()

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.player_entry),
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

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                label = { Text(text = stringResource(R.string.email)) },
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

            if (isRegisterMode) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    label = { Text(stringResource(R.string.username)) },
                    value = username,
                    onValueChange = { username = it },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = GoldAccent) },
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
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                label = { Text(text = stringResource(R.string.password)) },
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

            Button(
                onClick = {
                    if (isRegisterMode) {
                        if (username.isNotEmpty()) {
                            viewModel.registerUser(email, password, username)
                        }
                    } else {
                        coroutineScope.launch {
                            val result = viewModel.loginUser(email, password)
                            if (result?.user != null) {
                                onLoginSuccess()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonRed, contentColor = GoldAccent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(text = if (isRegisterMode) stringResource(R.string.register) else stringResource(R.string.login), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isRegisterMode) stringResource(R.string.already_have_an_account_login) else stringResource(id = R.string.need_an_account_register),
                color = GoldAccent,
                modifier = Modifier.clickable { isRegisterMode = !isRegisterMode }
            )
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
                is LoginUiState.LoginSuccess -> Text(stringResource(R.string.login_successful), color = GoldAccent)
                is LoginUiState.RegisterSuccess -> Text(stringResource(R.string.registration_successful), color = GoldAccent)
            }
        }
    }
}

@Composable
private fun CardSuitDecorations() {
    val suitColor = Color.Black.copy(alpha = 0.2f)
    val suitSize = 50.sp
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(R.string.spade), fontSize = suitSize, color = suitColor, modifier = Modifier.align(Alignment.TopStart))
        Text(stringResource(R.string.heart), fontSize = suitSize, color = suitColor, modifier = Modifier.align(Alignment.TopEnd))
        Text(stringResource(R.string.club), fontSize = suitSize, color = suitColor, modifier = Modifier.align(Alignment.BottomStart))
        Text(stringResource(R.string.diamond), fontSize = suitSize, color = suitColor, modifier = Modifier.align(Alignment.BottomEnd))
    }
}
