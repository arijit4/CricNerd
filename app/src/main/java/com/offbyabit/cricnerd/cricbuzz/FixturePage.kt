package com.offbyabit.cricnerd.cricbuzz

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class FixturePage {
    private var html: Document? = null

    fun getUpcomingMatches(): MutableList<Match> {
        try {
            this.html = Jsoup.connect("https://sports.ndtv.com/cricket/schedules-fixtures").get()
        } catch (_: Exception) {
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
                ) {
                    "live"
                } else {
                    "recent"
                }
            }
            val matchId = box.attr("href").split("-").last()

            /*val participants: MutableList<String> = mutableListOf()
            box.select("span.img-gr_sq").forEach { tag ->
                participants.add(tag.select("img").attr("alt"))
            }*/

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
                    status = details.getOrElse(1) { "" }
                )
            )
        }

        matches.forEach {
            it.print()
        }

        return matches
    }

    fun getRecentMatches(): MutableList<Match> {
        try {
            this.html = Jsoup.connect("https://sports.ndtv.com/cricket/results").get()
        } catch (_: Exception) {
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
                status = if (details[0] == "Play In Progress"
                    || details[0] == "Innings Break"
                ) {
                    "live"
                } else {
                    "recent"
                }
            }

            var matchId = box.attr("href")
            matchId = matchId
                .subSequence(0 until matchId.lastIndexOf("?"))
                .split("-").last()

            /*val participants: MutableList<String> = mutableListOf()
            box.select("span.img-gr_sq").forEach { tag ->
                participants.add(tag.select("img").attr("alt"))
            }*/

            val teamNamesAndFlag = box.select("span.img-gr_sq")
            val teamScores = box.select("span.scr_tm-run")

//            println(teamNamesAndFlag)

            val participants = listOf(
                Participant(
                    full_name = teamNamesAndFlag[0].select("img").attr("title"),
                    flag_url = teamNamesAndFlag[0].select("img").attr("data-src"),
                    id = teamNamesAndFlag[0].select("img").attr("data-src").getTeamId(),
                    current_score = teamScores.getOrElse(0) { Element("<span></span>") }.text()
                ),
                Participant(
                    full_name = teamNamesAndFlag[1].select("img").attr("title"),
                    flag_url = teamNamesAndFlag[1].select("img").attr("data-src"),
                    id = teamNamesAndFlag[1].select("img").attr("data-src").getTeamId(),
                    current_score = teamScores.getOrElse(1) { Element("<span></span>") }.text()
                )
            )

            matches.add(
                Match(
                    heading = heading.replace("&nbsp", " "),
                    match_id = matchId,
                    participants = participants,
                    match_type = status,
                    starts_at = "",
                    status = details[0]
                )
            )
//            println("hola")
        }

        /*matches.forEach {
            it.print()
        }*/

        return matches
    }

    fun getLiveMatches(): MutableList<Match> {
        try {
            this.html = Jsoup.connect("https://sports.ndtv.com/cricket/live-scores").get()
        } catch (_: Exception) {
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
                status = if (details[0] == "Play In Progress"
                    || details[0] == "Innings Break"
                ) {
                    "live"
                } else {
                    "recent"
                }
            }
            val matchId = box.attr("href").split("-").last()

            /* val participants: MutableList<String> = mutableListOf()
             box.select("span.img-gr_sq").forEach { tag ->
                 participants.add(tag.select("img").attr("alt"))
             }*/

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
                    status = details.getOrElse(1) { "" }
                )
            )
        }

        matches.filter { it.match_type == "live" }
        matches.forEach {
            it.print()
        }

        return matches
    }
}