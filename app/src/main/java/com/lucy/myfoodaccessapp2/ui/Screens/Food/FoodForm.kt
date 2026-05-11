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
                Toast.makeText(context, "Posted successfully!", Toast.LENGTH_SHORT).show()
                viewModel.resetUploadState()
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("What are you sharing?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Provide details so others can find it", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Food Title *") },
                placeholder = { Text("e.g. Fresh Bananas") },
                leadingIcon = { Icon(Icons.Default.Restaurant, null) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isUploading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Pickup Location *") },
                placeholder = { Text("e.g. Westlands") },
                leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isUploading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description *") },
                placeholder = { Text("Tell us more about the food...") },
                leadingIcon = { Icon(Icons.Default.Description, null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                enabled = !isUploading
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    when {
                        title.isBlank() -> Toast.makeText(context, "Title required", Toast.LENGTH_SHORT).show()
                        location.isBlank() -> Toast.makeText(context, "Location required", Toast.LENGTH_SHORT).show()
                        description.isBlank() -> Toast.makeText(context, "Description required", Toast.LENGTH_SHORT).show()
                        else -> viewModel.postFood(title, location, description)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isUploading
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Post Food Now", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}