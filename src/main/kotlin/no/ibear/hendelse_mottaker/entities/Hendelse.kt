package no.ibear.hendelse_mottaker.entities

import java.sql.Timestamp

data class Hendelse(
    val id: Int,
    val type: String,
    val payload: String,
    val status: Int,
    val created_at: Timestamp,
    val updated_at: Timestamp,
)