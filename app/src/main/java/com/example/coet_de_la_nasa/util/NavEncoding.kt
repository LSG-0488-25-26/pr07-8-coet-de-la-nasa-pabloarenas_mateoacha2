package com.example.coet_de_la_nasa.util

import java.net.URLDecoder
import java.net.URLEncoder

fun encodeForNav(value: String): String = URLEncoder.encode(value, Charsets.UTF_8.name())

fun decodeFromNav(value: String): String = URLDecoder.decode(value, Charsets.UTF_8.name())

