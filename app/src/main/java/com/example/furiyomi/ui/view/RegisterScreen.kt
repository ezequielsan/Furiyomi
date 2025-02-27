package com.example.furiyomi.ui.view

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.ui.theme.White100
import com.example.furiyomi.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun RegisterScreen(viewModel: AuthViewModel, navController: NavController) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var keepSignedIn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true    // Ativar animação ao entrar na tela
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent((result.data))
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                viewModel.loginWithGoogle(idToken) { success ->
                    if (success) {
                        navController.navigate("library")
                    } else {
                        Toast.makeText(context, "Erro ao fazer login com Google", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    Box(modifier = Modifier.fillMaxSize(), /*contentAlignment = Alignment.Center*/) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically() + fadeIn()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Create an account",
                    style = FuriyomiTheme.typography.titleLarge,
                    fontSize = 30.sp,
                    color = FuriyomiTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(38.dp))

                Text(
                    "Name",
                    style = FuriyomiTheme.typography.labelNormal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                CustomTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "John Doe"
                )

                Spacer(modifier = Modifier.height(18.dp))


                Text(
                    "Email Address",
                    style = FuriyomiTheme.typography.labelNormal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "hello@example.com"
                )

                Spacer(modifier = Modifier.height(18.dp))


                Text(
                    "Password",
                    style = FuriyomiTheme.typography.labelNormal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    isPassword = true,
                    placeholder = "**********"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.register(email, password, name) { success ->
                            if (success) {
                                // Navega para login após sucesso
                                navController.navigate("login")
                            } else {
                                Toast.makeText(
                                    context,
                                    "Erro no cadastro",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = FuriyomiTheme.colorScheme.primary),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Text(
                        "Sign up",
                        color = White100,
                        style = FuriyomiTheme.typography.body,
                        modifier = Modifier.padding(6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = { navController.navigate("login") }) {
                        Text(
                            "Already have an account? Sign in here",
                            color = FuriyomiTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}