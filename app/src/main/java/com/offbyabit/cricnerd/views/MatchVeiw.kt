package com.offbyabit.cricnerd.views
//
//import android.content.Context
//import androidx.compose.material3.Card
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.tooling.preview.Preview
////import com.google.gson.Gson
////import com.offbyabit.cricketnerd.Match
//import com.offbyabit.cricketnerd.Matches
//import java.io.File
//
////@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun MatchCard(match: Match) {
//    Card {
//
//    }
//}
////
////@Preview(showSystemUi = true, showBackground = true)
////@Composable
////fun Prev() {
////    val matches = matchesFromJSON(readFile("match_json.json"))
////    MatchCard(matches[0])
////}
//
//fun readFile(context: Context, fileName: String): String {
//    return context.assets.open(fileName).bufferedReader().use { it.readText() }
//}
//
//fun matchesFromJSON(data: String): List<Match> {
//    return Gson().fromJson(data, Matches::class.java).matches
//}