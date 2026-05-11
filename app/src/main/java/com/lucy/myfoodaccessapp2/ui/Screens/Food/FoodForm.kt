package com.lucy.myfoodaccessapp2.ui.Screens.Food

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodForm(
    navController: NavController,
    viewModel: FoodViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val isUploading by viewModel.isUploading.collectAsState()
    val uploadSuccess by viewModel.uploadSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uploadSuccess, errorMessage) {
        when {
            uploadSuccess == true -> {
                Toast.makeText(context, "Food posted successfully!", Toast.LENGTH_SHORT).show()
                viewModel.resetUploadState()

                // NEW: Set refresh flag for previous screen
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("refresh_posts", true)

                navController.popBackStack()
            }
            uploadSuccess == false && errorMessage != null -> {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                viewModel.resetUploadState()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post Available Food") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "What are you sharing?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Provide details so others can find it",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Food Title *") },
                placeholder = { Text("e.g. Fresh Bananas, Unopened Milk") },
                leadingIcon = { Icon(Icons.Default.Restaurant, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                enabled = !isUploading,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Pickup Location *") },
                placeholder = { Text("e.g. Westlands, Near the Post Office") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                enabled = !isUploading,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description *") },
                placeholder = { Text("Tell us more about the food, expiry date, etc.") },
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                shape = MaterialTheme.shapes.medium,
                enabled = !isUploading
            )

            Spacer(modifier = Modifier.height(32.dp))

            errorMessage?.let { error ->
                if (uploadSuccess == false) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val trimmedTitle = title.trim()
                    val trimmedLocation = location.trim()
                    val trimmedDescription = description.trim()

                    when {
                        trimmedTitle.isBlank() -> {
                            Toast.makeText(context, "Food title is required", Toast.LENGTH_SHORT).show()
                        }
                        trimmedLocation.isBlank() -> {
                            Toast.makeText(context, "Pickup location is required", Toast.LENGTH_SHORT).show()
                        }
                        trimmedDescription.isBlank() -> {
                            Toast.makeText(context, "Description is required", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            viewModel.postFood(trimmedTitle, trimmedLocation, trimmedDescription)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !isUploading
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Post Food Now",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}