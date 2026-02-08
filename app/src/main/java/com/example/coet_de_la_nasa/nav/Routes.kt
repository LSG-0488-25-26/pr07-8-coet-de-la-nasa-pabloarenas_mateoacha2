package com.example.coet_de_la_nasa.nav

sealed class Routes(val route: String) {
    object LeagueList : Routes("league_list")

    object LeagueDetail : Routes("league_detail/{mbid}/{title}/{artistName}") {
        fun createRoute(mbid: String, title: String, artistName: String): String {
            return "league_detail/$mbid/$title/$artistName"
        }
    }
}
