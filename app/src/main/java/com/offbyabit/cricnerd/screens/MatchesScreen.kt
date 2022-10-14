package com.offbyabit.cricnerd.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.offbyabit.cricnerd.cricbuzz.FixturePage
import com.offbyabit.cricnerd.cricbuzz.Match
import com.offbyabit.cricnerd.common.BottomNavigationBar
import com.offbyabit.cricnerd.common.MatchCard
import com.offbyabit.cricnerd.common.isNdtvConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = "Recent Matches")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, selected = 1)
        }
    ) { padding ->
        SettingScreenContent(navController, padding)
    }
}

@Composable
fun SettingScreenContent(navController: NavHostController, paddingValues: PaddingValues) {
    var recentMatches by remember { mutableStateOf(listOf<Match>()) }
    val scope = rememberCoroutineScope()
    var connected by remember { mutableStateOf(isNdtvConnected()) }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        LaunchedEffect(key1 = true) {
            withContext(Dispatchers.IO) {
                recentMatches = FixturePage().getRecentMatches()
            }
        }
        val context = LocalContext.current
        if (!connected) {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Can't wait to reach you"
                    )
                    Button(onClick = {
                        if (isNdtvConnected()) {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    recentMatches = FixturePage().getRecentMatches()
                                    connected = true
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Still no connection :(",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                        Text(text = "Try again to connect?")
                    }
                }
            }
        } else {
            if (recentMatches.isNotEmpty()) {
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(recentMatches) { match ->
                            MatchCard(navController = navController, match = match)
                        }
                    }
                }
            }
        }
    }
}
