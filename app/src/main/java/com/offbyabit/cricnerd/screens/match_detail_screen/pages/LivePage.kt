package com.offbyabit.cricnerd.screens.match_detail_screen.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.offbyabit.cricnerd.common.fetchJSON
import com.offbyabit.cricnerd.common.isNdtvConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.math.roundToInt

@Composable
fun LivePage(
    matchId: String// = "nzpk10112022213894"
) {
    val universalGap = 18.dp
    val matchDetailUrl =
        "https://sdata.ndtv.com/sportz/cricket/xml/{match_id}.json?t=8{current_time}"
            .replace("{match_id}", matchId)

    var detailJsonText by remember { mutableStateOf("") }

    LaunchedEffect(key1 = System.currentTimeMillis()) {
        withContext(Dispatchers.IO) {
            while (true) {
                if (isNdtvConnected()) {
                    detailJsonText =
                        fetchJSON(
                            matchDetailUrl.replace(
                                "{current_time}",
                                System.currentTimeMillis().toString()
                            )
                        )
                    delay(15_000)
                } else {
                    delay(5_000)
                }
            }
        }
    }
    if (detailJsonText.isNotEmpty()) {
        val obj = JSONObject(detailJsonText)

//        val homeTeamCode = obj.getJSONObject("Matchdetail").getString("Team_Home")
//        val awayTeamCode = obj.getJSONObject("Matchdetail").getString("Team_Away")

        Column {
            Text(
                text = obj.getJSONObject("Matchdetail").getString("Status"),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            var inningsCount = 0
            if (obj.has("Innings")) {
                inningsCount = obj.getJSONArray("Innings").length()
            }
            if (inningsCount != 0) {
                val currentInnings = obj
                    .getJSONArray("Innings")
                    .getJSONObject(inningsCount - 1)

                val battingTeamCode = currentInnings
                    .getString("Battingteam")

                val battingTeamName = obj
                    .getJSONObject("Teams")
                    .getJSONObject(battingTeamCode)
                    //                    .getString("Name_Full")
                    .getString("Name_Short")

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // status Column...
                        Column {
                            Row {
                                // Add team-image here...
                                Text(
                                    text = battingTeamName,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                            }
                            Text(
                                currentInnings.getString("Total") + "-" +
                                        currentInnings.getString("Wickets") + " " +
                                        "(${currentInnings.getString("Overs")})"
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Column(
                            modifier = Modifier
                                .padding(start = universalGap)
                        ) {
                            RunRateView(currentInnings)
                        }
                    }

                    if (obj.has("Matchequation")
                        && obj.getJSONObject("Matchequation").has("Equation")
                    ) {
                        Text(
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            text = obj.getJSONObject("Matchequation").getString("Equation")
                        )
                    }


                    val batsmen = getBatsmen(currentInnings = currentInnings, matchDetails = obj)
                    val bowlers = getBowlers(currentInnings = currentInnings, matchDetails = obj)

                    if (batsmen[1].isNotEmpty()) {
                        BatsmanBox(activeBatsmen = batsmen[1])

                    }
                    if (bowlers[1].isNotEmpty()) {
                        /*Spacer(modifier = Modifier.height(universalGap))
                        Divider(
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )*/
                        BowlerBox(activeBowlers = bowlers[1])
                    }
                }

//                Spacer(modifier = Modifier.height(universalGap))
//                DebugJson(json = obj, title = "Match JSON")
                Spacer(modifier = Modifier.height(universalGap))
                DebugJson(json = currentInnings, title = "Current Innings JSON")
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Can't wait to stream live sports.")
        }
    }
}

@Composable
fun RunRateView(innings: JSONObject) {
    val universalGap = 18.dp
    Row {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text("CRR")
            Text(innings.getString("Runrate"))
        }
        if (innings.getString("Number").equals("Second")) {
            Spacer(modifier = Modifier.width(universalGap))
            Column(
                horizontalAlignment = Alignment.End
            ) {
                val ballsDone = innings.getString("Total_Balls_Bowled").toFloat()
                val totalBalls = innings.getString("AllotedBalls").toFloat()
                val target = innings.getString("Target").toFloat()
                val runsMade = innings.getString("Total").toFloat()

                val ballsLeft = totalBalls - ballsDone
                val neededRuns = target - runsMade

                if (ballsLeft > 0 && neededRuns >= 0) {
                    val rrr = (neededRuns / ballsLeft * 6_00).roundToInt() / 100f

                    Text("Req")
                    Text(rrr.toString())
                }
            }
        }
    }
}

@Composable
fun BatsmanDetailColumn(
    key: String,
    getter: (Batsman) -> String,
    batsmen: List<Batsman>,
    specificWidth: Boolean = false
) {
    if (batsmen.isNotEmpty()) {
        if (specificWidth) {
            Column(
                modifier = Modifier.fillMaxWidth(0.3f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    style = MaterialTheme.typography.bodySmall,
                    text = key
                )
                batsmen.forEach {
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = getter(it)
                    )
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    style = MaterialTheme.typography.bodySmall,
                    text = key
                )
                batsmen.forEach {
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = getter(it)
                    )
                }
            }
        }
    }
}

@Composable
fun BowlerDetailColumn(
    key: String,
    getter: (Bowler) -> String,
    bowlers: List<Bowler>,
    specificWidth: Boolean = false
) {
    if (bowlers.isNotEmpty()) {
        if (specificWidth) {
            Column(
                modifier = Modifier.fillMaxWidth(0.3f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    style = MaterialTheme.typography.bodySmall,
                    text = key
                )
                bowlers.forEach {
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = getter(it)
                    )
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    style = MaterialTheme.typography.bodySmall,
                    text = key
                )
                bowlers.forEach {
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = getter(it)
                    )
                }
            }
        }
    }
}

@Composable
fun BatsmanBox(activeBatsmen: MutableList<Batsman>) {
    val universalGap = 18.dp
    Spacer(modifier = Modifier.height(universalGap))
    Divider(
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    )
    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.4f)) {
            activeBatsmen.forEach {
                Text(
                    style = MaterialTheme.typography.bodySmall,
                    text = it.name + if (it.isOnStrike) "*" else "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.height(universalGap))
        Spacer(modifier = Modifier.weight(1f))
        BatsmanDetailColumn(
            key = "R",
            getter = { b: Batsman -> b.runs },
            batsmen = activeBatsmen
        )
        Spacer(modifier = Modifier.weight(1f))
        BatsmanDetailColumn(
            key = "B",
            getter = { b: Batsman -> b.balls },
            batsmen = activeBatsmen
        )
        /*Spacer(modifier = Modifier.weight(1f))
        BatsmanDetailColumn(
            key = "D",
            getter = { b: Batsman -> b.dots },
            batsmen = activeBatsmen
        )*/
        Spacer(modifier = Modifier.weight(1f))
        BatsmanDetailColumn(
            key = "6s",
            getter = { b: Batsman -> b.sixes },
            batsmen = activeBatsmen
        )
        Spacer(modifier = Modifier.weight(1f))
        BatsmanDetailColumn(
            key = "4s",
            getter = { b: Batsman -> b.fours },
            batsmen = activeBatsmen
        )
        Spacer(modifier = Modifier.weight(1f))
        BatsmanDetailColumn(
            key = "SR",
            getter = { b: Batsman -> b.strikeRate },
            batsmen = activeBatsmen,
            specificWidth = true
        )
    }
}

@Composable
fun BowlerBox(activeBowlers: MutableList<Bowler>) {
    val universalGap = 18.dp
    Spacer(modifier = Modifier.height(universalGap))
    Divider(
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    )
    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.4f)) {
            activeBowlers.forEach {
                Text(
                    style = MaterialTheme.typography.bodySmall,
                    text = it.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.height(universalGap))
        Spacer(modifier = Modifier.weight(1f))
        BowlerDetailColumn(
            key = "O",
            getter = { b: Bowler -> b.overs },
            bowlers = activeBowlers
        )
        Spacer(modifier = Modifier.weight(1f))
        BowlerDetailColumn(
            key = "M",
            getter = { b: Bowler -> b.maidens },
            bowlers = activeBowlers
        )
        Spacer(modifier = Modifier.weight(1f))
        BowlerDetailColumn(
            key = "R",
            getter = { b: Bowler -> b.runs },
            bowlers = activeBowlers
        )
        Spacer(modifier = Modifier.weight(1f))
        BowlerDetailColumn(
            key = "W",
            getter = { b: Bowler -> b.wickets },
            bowlers = activeBowlers
        )
        Spacer(modifier = Modifier.weight(1f))
        BowlerDetailColumn(
            key = "ER",
            getter = { b: Bowler -> b.economyRate },
            bowlers = activeBowlers,
            specificWidth = true
        )
        /*Spacer(modifier = Modifier.weight(1f))
        BowlerDetailColumn(
            key = "SR",
            getter = { b: Bowler -> b.strikeRate },
            batsmen = activeBatsmen
        )*/
    }
}

@Composable
fun DebugJson(json: JSONObject, title: String) {
    val universalGap = 18.dp
    var showDebugText by remember { mutableStateOf(false) }
    ElevatedCard {
        Column(
            modifier = Modifier.padding(universalGap)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { showDebugText = !showDebugText }) {
                    if (showDebugText) {
                        Icon(Icons.Rounded.ArrowUpward, contentDescription = null)
                    } else {
                        Icon(Icons.Rounded.ArrowDownward, contentDescription = null)
                    }
                }
            }
            AnimatedVisibility(visible = showDebugText) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = json.toString(8),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

fun getBatsmen(currentInnings: JSONObject, matchDetails: JSONObject): List<MutableList<Batsman>> {
    val teamId = currentInnings.getString("Battingteam")
    val players = matchDetails
        .getJSONObject("Teams")
        .getJSONObject(teamId)
        .getJSONObject("Players")

    val batsmen = currentInnings.getJSONArray("Batsmen")

    val data = mutableListOf<Batsman>()
    val active = mutableListOf<Batsman>()

    for (i in 0 until batsmen.length()) {
        val temp = batsmen.getJSONObject(i)
        data.add(
            Batsman(
                players.getJSONObject(temp.getString("Batsman")).getString("Name_Full"),
                temp.getString("Batsman"),
                temp.getString("Runs"),
                temp.getString("Balls"),
                temp.getString("Fours"),
                temp.getString("Sixes"),
                temp.getString("Dots"),
                temp.getString("Strikerate"),
                temp.has("Isbatting"),
                temp.has("Isonstrike"),
            )
        )

        if (data.last().isBatting) {
            active.add(data.last())
        }
    }
    return listOf(data, active)
}

fun getBowlers(currentInnings: JSONObject, matchDetails: JSONObject): List<MutableList<Bowler>> {
    val teamId = currentInnings.getString("Bowlingteam")
    val players = matchDetails
        .getJSONObject("Teams")
        .getJSONObject(teamId)
        .getJSONObject("Players")

    val bowlers = currentInnings.getJSONArray("Bowlers")

    val data = mutableListOf<Bowler>()
    val active = mutableListOf<Bowler>()

    for (i in 0 until bowlers.length()) {
        val temp = bowlers.getJSONObject(i)
        data.add(
            Bowler(
                name = players.getJSONObject(temp.getString("Bowler")).getString("Name_Full"),
                code = temp.getString("Bowler"),
                overs = temp.getString("Overs"),
                ballsBowled = temp.getString("Balls_Bowled"),
                maidens = temp.getString("Maidens"),
                economyRate = temp.getString("Economyrate"),
                wickets = temp.getString("Wickets"),
                runs = temp.getString("Runs"),
                isBowlingNow = temp.has("Isbowlingnow")
            )
        )

        if (data.last().isBowlingNow) {
            active.add(data.last())
        }
    }
    return listOf(data, active)
}

data class Batsman(
    val name: String,
    val code: String,
    val runs: String,
    val balls: String,
    val fours: String,
    val sixes: String,
    val dots: String,
    val strikeRate: String,
    val isBatting: Boolean,
    val isOnStrike: Boolean
)

data class Bowler(
    val name: String,
    val code: String,
    val overs: String,
    val ballsBowled: String,
    val maidens: String,
    val economyRate: String,
    val wickets: String,
    val runs: String,
    val isBowlingNow: Boolean,
)