package com.offbyabit.cricnerd.screens.match_detail_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.offbyabit.cricnerd.screens.match_detail_screen.pages.InfoPage
import com.offbyabit.cricnerd.screens.match_detail_screen.pages.LivePage
import com.offbyabit.cricnerd.screens.match_detail_screen.pages.SquadPage
import com.offbyabit.cricnerd.common.fetchJSON
import com.offbyabit.cricnerd.common.isNdtvConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient

var client = OkHttpClient()
const val match_stat_url = "https://sdata.ndtv.com/sportz/cricket/xml/{match_id}.json"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailScreen(
    navHostController: NavHostController,
    matchId: String
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Match Details",/*json
                            .getJSONObject("Matchdetail")
                            .getJSONObject("Series")
                            .getString("Series_short_display_name")*/
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.popBackStack()
                        }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            MatchDetailScreenContent(
                matchId = matchId,
                innerPadding = paddingValues
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailScreenContent(
    matchId: String,
    innerPadding: PaddingValues
) {
    Surface(
        modifier = Modifier
            .padding(innerPadding)
            .padding(start = 16.dp, end = 16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        var connected by remember { mutableStateOf(isNdtvConnected()) }
        val scope = rememberCoroutineScope()
        val url = match_stat_url.replace("{match_id}", matchId)
        var detailsJson by remember { mutableStateOf("") }

        val chipTitles = listOf("Info", "Live", "Scorecard", "Graph", "Squad", "Overs")
        val chipAvailability by remember {
            mutableStateOf(
                listOf(
                    true, // info page
                    true, // live page
                    false, // scorecard page
                    false, // graph page
                    true, // squad page
                    false // overs page
                )
            )
        }

        // info-page opens first if live-page is not available...
        // var chipSelectedIndex by remember { mutableStateOf(if (chipAvailability[1]) 1 else 0) }

        // live-page is under construction :)
        var chipSelectedIndex by remember { mutableStateOf(0) }

        if (connected) {
            LaunchedEffect(key1 = true) {
                withContext(Dispatchers.IO) {
                    detailsJson = fetchJSON(url)
                }
            }
            Column {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(chipTitles) { index, chip ->
                        FilterChip(
                            selected = (index == chipSelectedIndex),
                            enabled = chipAvailability[index],
                            onClick = { chipSelectedIndex = index },
                            label = { Text(text = chip) }
                        )
                    }
                }

                if (detailsJson.isNotEmpty()) {
                    when (chipSelectedIndex) {
                        0 -> InfoPage(detailsJson)
                        1 -> LivePage(matchId)
                        4 -> SquadPage(detailsJson)
                    }
                }
            }
        } else {
            val context = LocalContext.current
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
                                detailsJson = fetchJSON(url)
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
    }
}
