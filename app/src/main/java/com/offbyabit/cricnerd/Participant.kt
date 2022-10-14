package com.offbyabit.cricketnerd

data class Participant(
    val firstup: String,
    val highlight: String,
    val id: String,
    val name: String,
    val name_eng: String,
    val now: String,
    val players_involved: List<Any>,
    val short_name: String,
    val value: String
)