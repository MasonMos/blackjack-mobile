package hu.bme.ait.blackjack.ui.screen.loginscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import hu.bme.ait.blackjack.data.Player


sealed interface LoginUiState {
    object Init: LoginUiState
    object Loading: LoginUiState
    object RegisterSuccess: LoginUiState
    object LoginSuccess: LoginUiState
    data class Error(val errorMessage: String?): LoginUiState
}

class LoginViewModel : ViewModel() {

    var loginUiState: LoginUiState by mutableStateOf(LoginUiState.Init)
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var auth: FirebaseAuth

    init {
        auth = Firebase.auth
    }

    fun registerUser(email: String, password: String, username: String) {
        loginUiState = LoginUiState.Loading
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val userId = authResult.user?.uid ?: ""
                    val newPlayer = Player(
                        uid = userId,
                        email = email,
                        username = username,
                        balance = 500
                    )

                    // Save to Firestore
                    firestore.collection("players").document(userId).set(newPlayer)
                        .addOnSuccessListener {
                            loginUiState = LoginUiState.RegisterSuccess
                        }
                        .addOnFailureListener { e ->
                            loginUiState = LoginUiState.Error("Firestore Error: ${e.localizedMessage}")
                        }
                }
                .addOnFailureListener {
                    loginUiState = LoginUiState.Error(it.localizedMessage)
                }

        } catch (e: Exception) {
            loginUiState = LoginUiState.Error(e.localizedMessage)
        }
    }

    suspend fun loginUser(email: String, password: String) : AuthResult? {
        loginUiState = LoginUiState.Loading
        try {
            val result = auth.signInWithEmailAndPassword(email,password).await()
            if (result.user != null) {
                loginUiState = LoginUiState.LoginSuccess
            } else {
                loginUiState = LoginUiState.Error("Login Failed")
            }

            return result
        } catch (e: Exception) {
            loginUiState = LoginUiState.Error(e.localizedMessage)
            e.printStackTrace()
        }
        return null
    }
}

