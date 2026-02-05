package com.example.coet_de_la_nasa.nav

sealed class Routes(val route: String) {
    object LeagueList : Routes("league_list")

    object LeagueDetail : Routes("league_detail/{leagueId}/{leagueName}") {
        fun createRoute(leagueId: String, leagueName: String): String {
            return "league_detail/$leagueId/$leagueName"
        }
    }
}
