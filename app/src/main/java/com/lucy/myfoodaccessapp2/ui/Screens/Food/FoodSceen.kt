package com.lucy.myfoodaccessapp2.ui.Screens.Food

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lucy.myfoodaccessapp2.Data.Models.FoodPost
import com.lucy.myfoodaccessapp2.ui.Screens.Home.HomeViewModel
import com.lucy.myfoodaccessapp2.ui.Navigation.ROUTES

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val foodPosts by viewModel.foodPosts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // NEW: Watch for refresh flag from FoodForm
    val refreshNeeded = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("refresh_posts", false)
        ?.collectAsState()

    LaunchedEffect(refreshNeeded?.value) {
        if (refreshNeeded?.value == true) {
            viewModel.fetchPosts()
            // Clear the flag so it doesn't refresh again
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("refresh_posts", false)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Available Food",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(ROUTES.ADD_FOOD.path) },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Food") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (foodPosts.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No food posts available yet.")
                    Button(onClick = { viewModel.fetchPosts() }) {
                        Text("Refresh")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(foodPosts) { post ->
                        FoodPostCard(post = post)
                    }
                }
            }
        }
    }
}

@Composable
fun FoodPostCard(post: FoodPost) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = post.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = post.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            post.description?.let { desc ->
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } ?: Text(
                text = "No description provided",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}