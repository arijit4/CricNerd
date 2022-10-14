package com.offbyabit.cricnerd.screens.match_detail_screen.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.offbyabit.cricnerd.screens.match_detail_screen.DetailCard
import org.json.JSONObject

@Composable
fun InfoPage(detailsJson: String) {
    if (detailsJson.isNotEmpty()) {
        val json = JSONObject(detailsJson)//.getJSONObject("Matchdetail")
        LazyColumn {
            item { InfoCard(json = json) }
            item { Spacer(Modifier.height(12.dp)) }
            item { VenueCard(json = json) }
        }
    }
}

@Composable
fun InfoCard(json: JSONObject) {
    val matchDetailJson = json.getJSONObject("Matchdetail")
    var tossWonBy = matchDetailJson.getString("Tosswonby")

    if (tossWonBy.isNotEmpty()) {
        tossWonBy =
            json
                .getJSONObject("Teams")
                .getJSONObject(tossWonBy)
                .getString("Name_Full")
    }
    Text(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(top = 12.dp, bottom = 12.dp),
        text = "Match Info"
    )
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(8.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            DetailCard(
                key = "Match",
                detail = matchDetailJson
                    .getJSONObject("Match")
                    .getString("Number")
            )
            DetailCard(
                key = "Series",
                detail = matchDetailJson
                    .getJSONObject("Series")
                    .getString("Name")
            )

            /*val formatter = SimpleDateFormat("yyyy/MM/dd ")
            val text = "${
                matchDetailJson
                    .getJSONObject("Match")
                    .getString("Date")
            } ${
                matchDetailJson
                    .getJSONObject("Match")
                    .getString("Time") +
                        matchDetailJson
                            .getJSONObject("Match")
                            .getString("Offset")
            }"
            val date = formatter.parse(text)*/

            DetailCard(
                key = "Date",
                detail = matchDetailJson
                    .getJSONObject("Match")
                    .getString("Date")
            )
            DetailCard(
                key = "Time",
                detail = matchDetailJson
                    .getJSONObject("Match")
                    .getString("Time") +
                        matchDetailJson
                            .getJSONObject("Match")
                            .getString("Offset")
            )
            if (tossWonBy.isNotEmpty()) {
                DetailCard(
                    key = "Toss",
                    detail = "$tossWonBy won the toss and elected to ${
                        matchDetailJson.getString("Toss_elected_to")
                    } first"
                )
            }
            DetailCard(
                key = "Venue",
                detail = matchDetailJson
                    .getJSONObject("Venue")
                    .getString("Name")
            )
            DetailCard(
                key = "Umpires",
                detail = matchDetailJson
                    .getJSONObject("Officials")
                    .getString("Umpires")
            )
            DetailCard(
                key = "Referee",
                detail = matchDetailJson
                    .getJSONObject("Officials")
                    .getString("Referee"),
                shouldSpace = false
            )
        }
    }
}

@Composable
fun VenueCard(json: JSONObject) {
    val matchDetailJson = json.getJSONObject("Matchdetail")
    Text(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(top = 12.dp, bottom = 12.dp),
        text = "Venue Guide"
    )
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(8.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            DetailCard(
                key = "Stadium",
                detail = matchDetailJson
                    .getJSONObject("Venue")
                    .getString("Name")
            )
            DetailCard(
                key = "Country",
                detail = matchDetailJson
                    .getJSONObject("Venue")
                    .getString("Country")
            )
            var pitchSuitedFor = ""

            if (
                matchDetailJson
                    .getJSONObject("Venue")
                    .getJSONObject("Pitch_Detail").has("Pitch_Suited_For")
            ) {
                pitchSuitedFor = matchDetailJson
                    .getJSONObject("Venue")
                    .getJSONObject("Pitch_Detail").getString("Pitch_Suited_For")
            }
            if (pitchSuitedFor.isNotEmpty()) {
                DetailCard(
                    key = "Pitch",
                    detail = pitchSuitedFor
                )
            }
        }
    }
}
