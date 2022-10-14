package com.offbyabit.cricnerd.cricbuzz

class Match(
    var match_id: String,
    var heading: String,
    var venue: String = "should work on it", // fixme
    var participants: List<Participant> = listOf(),
    var match_type: String,
    var starts_at: String,
    var start_timestamp: Long = 0, // fixme too
    var status: String
) {
    fun print() {
        println(
            """
               match_id -> $match_id
               heading -> $heading
               participants -> $participants
               match_type -> $match_type
               starts_at -> $starts_at
               status -> $status
            """.trimIndent()
        )
    }
}

class Participant(
    var full_name: String,
    var short_name: String = "",
    var id: String = "",
    var flag_url: String,
    var current_score: String = "",
    var win_probability: String = ""
) {
    fun print() {
        println(
            """
                full_name -> "$full_name"
                short_name -> "$short_name"
                id -> "$id"
                flag_url -> "$flag_url"
                current_score -> "$current_score"
                win_probability -> "$win_probability"
            """.trimIndent()
        )
    }
}