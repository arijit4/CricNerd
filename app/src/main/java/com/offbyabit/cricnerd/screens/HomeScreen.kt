package com.offbyabit.cricnerd.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.offbyabit.cricnerd.cricbuzz.Homepage
import com.offbyabit.cricnerd.cricbuzz.Match
import com.offbyabit.cricnerd.common.BottomNavigationBar
import com.offbyabit.cricnerd.common.MatchCard
import com.offbyabit.cricnerd.common.isNdtvConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "CrickNerd",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, selected = 0)
        },
        content = { p ->
            LazyColumn {
                item {
                    HomeScreenContent(navController, p)
                }
            }
        }
    )
}

@Composable
fun HomeScreenContent(navController: NavHostController, innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        var matches by remember { mutableStateOf(listOf<Match>()) }

        val scope = rememberCoroutineScope()
        var connected by remember { mutableStateOf(isNdtvConnected()) }

        LaunchedEffect(key1 = true) {
            withContext(Dispatchers.IO) {
                matches = Homepage().getMatches()
            }
        }
        val context = LocalContext.current
        if (!connected) {
            Column(
                modifier = Modifier.fillMaxSize(),
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
                                matches = Homepage().getMatches()
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

        } else {
            if (matches.isNotEmpty()) {
                Text(
//                    modifier = Modifier.padding(12.dp),
                    text = "Featured matches",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(matches) { match ->
                            MatchCard(
                                navController = navController,
                                match = match,
                                screenFraction = 0.8f
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            /*Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    navController.navigate(NavigationRoot.MatchesScreen.route)
                }) {
                Text("Recent matches")
            }*/
        }
    }
}
/*
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun dj() {
    LargeTopAppBar(
        title = { Text("hello") }
    )
    Text(text = "Hello, kitty!", modifier = Modifier.padding(start = 16.dp))
}*/

