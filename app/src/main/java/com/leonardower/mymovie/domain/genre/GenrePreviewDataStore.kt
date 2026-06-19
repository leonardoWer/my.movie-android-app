package com.leonardower.mymovie.domain.genre

fun getGenrePreviewImages(genreName: String): List<String> {
    return when (genreName.lowercase()) {
        "драма" -> listOf("drama__1.png", "drama__2.png")
        "комедия" -> listOf("comedy__1.png", "comedy__2.png")
        "триллер" -> listOf("thriller__1.png", "thriller__2.png")
        "фантастика" -> listOf("fant__1.png", "fant__2.png")
        "боевик" -> listOf("action__1.png", "action__2.png")
        "мелодрама", "любовь" -> listOf("love__1.png", "love__2.png")
        "ужасы" -> listOf("horror__1.png", "horror__2.png")
        "спорт" -> listOf("sport__1.png", "sport__2.png")
        "детектив" -> listOf("detective__1.png", "detective__2.png")
        "мультфильм" -> listOf("mult__1.png", "mult__2.png")
        "мультсериал", "Аниме" -> listOf("multserial__1.png", "multserial__2.png")
        "музыка" -> listOf("music__1.png", "music__2.png")
        else -> listOf("default__1.png", "default__2.png")
    }
}