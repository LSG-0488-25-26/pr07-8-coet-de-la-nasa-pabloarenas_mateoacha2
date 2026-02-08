package com.example.coet_de_la_nasa.nav

sealed class Routes(val route: String) {
    object LeagueList : Routes("league_list")
    object Colleccio : Routes("colleccio")

    object ColleccioDetail : Routes("colleccio/{collectionId}") {
        fun createRoute(collectionId: Long): String = "colleccio/$collectionId"
    }

    object LeagueDetail : Routes("league_detail/{mbid}/{title}/{artistName}") {
        fun createRoute(mbid: String, title: String, artistName: String): String {
            return "league_detail/$mbid/$title/$artistName"
        }
    }
}
