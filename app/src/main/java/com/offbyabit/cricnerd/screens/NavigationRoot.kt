package com.offbyabit.cricnerd.screens

const val MATCH_ID_KEY = "match_id_key"

sealed class NavigationRoot(val route: String) {
    object MainScreen : NavigationRoot("main_screen")
    object MatchesScreen : NavigationRoot("matches_screen")
    object SettingScreen : NavigationRoot("setting_screen")

    object MatchDetailScreen : NavigationRoot("match_detail_screen/{$MATCH_ID_KEY}") {
        fun passId(id: String): String {
            return MatchDetailScreen.route.replace("{$MATCH_ID_KEY}", id)
        }
    }
}
