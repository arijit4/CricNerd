package com.offbyabit.cricnerd.screens.match_detail_screen.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.json.JSONObject

@Composable
fun SquadPage(
    detailJson: String
) {
    val obj = JSONObject(detailJson)

    val homeTeamCode = obj.getJSONObject("Matchdetail").getString("Team_Home")
    val awayTeamCode = obj.getJSONObject("Matchdetail").getString("Team_Away")

    val teams = obj.getJSONObject("Teams")

    val homeTeam = teams.getJSONObject(homeTeamCode)
    val awayTeam = teams.getJSONObject(awayTeamCode)

    Row(
        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(0.5f),
            text = homeTeam.getString("Name_Full"),
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = awayTeam.getString("Name_Full"),
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis
        )
    }

    LazyColumn(
        modifier = Modifier.clip(RoundedCornerShape(12.dp))
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    val players = homeTeam.getJSONObject("Players")
                    val playerKeys = players.names()

                    for (i in 0 until playerKeys!!.length()) {
                        val player = players
                            .getJSONObject(playerKeys.get(i).toString())
                        HomePlayerCard(player = player)
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    val players = awayTeam.getJSONObject("Players")
                    val playerKeys = players.names()
                    for (i in 0 until playerKeys!!.length()) {
                        val player = players
                            .getJSONObject(playerKeys.get(i).toString())

                        AwayPlayerCard(player = player)
                    }
                }
            }
        }
    }
}

@Composable
fun HomePlayerCard(player: JSONObject) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = player
                    .getString("Name_Short") +
                        if (player.has("Iscaptain")) {
                            " (C)"
                        } else if (player.has("Iskeeper")) {
                            " (WK)"
                        } else {
                            ""
                        },
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = when (player.getString("Skill").toInt()) {
                    Skill.BATTER.ordinal -> "Batsman"
                    Skill.BOWLER.ordinal -> "Bowler"
                    Skill.KEEPER.ordinal -> "Wicket Keeper"
                    Skill.ALL_ROUNDER.ordinal -> "All-Rounder"
                    else -> "Fielder :)"
                },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun AwayPlayerCard(player: JSONObject) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = player
                    .getString("Name_Short") +
                        if (player.has("Iscaptain")) {
                            " (C)"
                        } else if (player.has("Iskeeper")) {
                            " (WK)"
                        } else {
                            ""
                        },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = when (player.getString("Skill").toInt()) {
                    Skill.BATTER.ordinal -> "Batsman"
                    Skill.BOWLER.ordinal -> "Bowler"
                    Skill.KEEPER.ordinal -> "Wicket Keeper"
                    Skill.ALL_ROUNDER.ordinal -> "All-Rounder"
                    else -> "Fielder :)"
                },
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End
            )
        }
    }
}

enum class Skill {
    FIELDER, BATTER, BOWLER, ALL_ROUNDER, KEEPER,
}