package com.example.coet_de_la_nasa.model

import com.google.gson.annotations.SerializedName

data class LeagueListResponse(
    val status: Boolean,
    val data: List<League>
)

data class LeagueDetailResponse(
    val status: Boolean,
    val data: League
)

data class League(
    val id: String,
    val name: String,
    val slug: String,
    val abbr: String,
    val logos: LeagueLogos
)

data class LeagueLogos(
    val light: String,
    val dark: String
)
