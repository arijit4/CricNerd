package com.offbyabit.cricnerd.cricbuzz

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class Homepage {
    private var html: Document? = null

    init {
        try {
            this.html = Jsoup.connect("https://sports.ndtv.com/cricket/live-scores").get()
        } catch (_: Exception) {

        }

    }

    fun getMatches(): MutableList<Match> {
        if (this.html == null) {
            return mutableListOf()
        }
        val matchCards = this.html!!.select("a.sp-scr_lnk")

        val matches: MutableList<Match> = mutableListOf()

        for (box in matchCards) {
            var heading = box.select("div.FL-flp_lt").text()
            var status = "upcoming"

            val details: MutableList<String> = mutableListOf()
            box.select("div.scr_dt-red").forEach {
                details.add(it.text())
            }
            if (heading.isEmpty()) {
                heading = box.select("div.scr_txt-ony").text()
                status = if (details[0].contains("Play In Progress")
                    || details[0].contains("Innings Break")
                    || details[0].contains("elected to")
                    || details[0].contains("Rain Stoppage")
                ) {
                    "live"
                } else {
                    "recent"
                }
            }
            val matchId = box.attr("href").split("-").last()

            val teamNamesAndFlag = box.select("span.img-gr_sq")
            val teamScores = box.select("span.scr_tm-run")

            val participants = listOf(
                Participant(
                    full_name = teamNamesAndFlag[0].select("img").attr("alt"),
                    flag_url = teamNamesAndFlag[0].select("img").attr("src"),
                    id = teamNamesAndFlag[0].select("img").attr("src").getTeamId(),
                    current_score = teamScores.getOrElse(0) { Element("<span></span>") }.text()
                ),
                Participant(
                    full_name = teamNamesAndFlag[1].select("img").attr("alt"),
                    flag_url = teamNamesAndFlag[1].select("img").attr("src"),
                    id = teamNamesAndFlag[1].select("img").attr("src").getTeamId(),
                    current_score = teamScores.getOrElse(1) { Element("<span></span>") }.text()
                )
            )

            matches.add(
                Match(
                    heading = heading.replace("&nbsp", " "),
                    match_id = matchId,
                    participants = participants,
                    match_type = status,
                    starts_at = details[0],
                    status = details.getOrElse(1) { " " }
                )
            )
        }

        matches.forEach {
            println(it.participants[0])
        }
//        var c = 1


        return matches
    }
}

fun String.getTeamId(): String {
    val x = this.lastIndexOf("flags/") + "flags/".length
    val y = this.lastIndexOf(".")

    return this.substring(x, y)
}