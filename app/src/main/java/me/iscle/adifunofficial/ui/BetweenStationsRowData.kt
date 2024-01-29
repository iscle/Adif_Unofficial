package me.iscle.adifunofficial.ui

data class BetweenStationsRowData(
    val originTime: Long,
    val destinationTime: Long,
    val destinationName: String,
    val line: String,
    val originPlatform: String,
)