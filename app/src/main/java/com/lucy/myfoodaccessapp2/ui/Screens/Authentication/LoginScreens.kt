package com.lucy.myfoodaccessapp2.ui.Screens.Authentication

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lucy.myfoodaccessapp2.ui.Navigation.ROUTES

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthResult.Success -> {
                navController.navigate(ROUTES.HOME.path) {
                    popUpTo(ROUTES.LOGIN.path) { inclusive = true }
                }
                viewModel.resetState()
            }
            is AuthResult.Error -> {
                Toast.makeText(context, (authState as AuthResult.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    // NEW: Black background for entire screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)  // ← BLACK BACKGROUND
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome Back",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White  // ← WHITE TEXT
        )
        Text(
            text = "Sign in to continue sharing",
            fontSize = 16.sp,
            color = Color.Gray  // ← GRAY SUBTITLE (visible on black)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Email field - wider and white text
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address", color = Color.Gray) },  // ← GRAY LABEL
            leadingIcon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    tint = Color.White  // ← WHITE ICON
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),  // ← TALLER FIELD for better visibility
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            enabled = !isLoading,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,      // ← WHITE TYPED TEXT
                unfocusedTextColor = Color.White,    // ← WHITE TYPED TEXT
                focusedBorderColor = Color.White,    // ← WHITE BORDER
                unfocusedBorderColor = Color.Gray,   // ← GRAY BORDER
                focusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),  // ← DARK BG
                unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.2f) // ← DARK BG
            ),
            singleLine = true  // ← PREVENTS MULTILINE, ALLOWS LONG EMAILS
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password field - white text
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.Gray) },  // ← GRAY LABEL
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.White  // ← WHITE ICON
                )
            },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = null,
                        tint = Color.White  // ← WHITE ICON
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),  // ← TALLER FIELD
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            enabled = !isLoading,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,      // ← WHITE TYPED TEXT
                unfocusedTextColor = Color.White,    // ← WHITE TYPED TEXT
                focusedBorderColor = Color.White,    // ← WHITE BORDER
                unfocusedBorderColor = Color.Gray,   // ← GRAY BORDER
                focusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
                unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.2f)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { /* Navigate to Forgot Password */ },
            modifier = Modifier.align(Alignment.End),
            enabled = !isLoading
        ) {
            Text("Forgot Password?", color = Color.White)  // ← WHITE TEXT
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    viewModel.signIn(email, password)
                } else {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,      // ← WHITE BUTTON
                contentColor = Color.Black        // ← BLACK TEXT ON BUTTON
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))  // ← BLACK SPINNER
            } else {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)  // ← BLACK TEXT
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account?", color = Color.Gray)  // ← GRAY TEXT
            TextButton(
                onClick = { navController.navigate(ROUTES.SIGNUP.path) },
                enabled = !isLoading
            ) {
                Text("Sign Up", fontWeight = FontWeight.Bold, color = Color.White)  // ← WHITE TEXT
            }
        }
    }
}