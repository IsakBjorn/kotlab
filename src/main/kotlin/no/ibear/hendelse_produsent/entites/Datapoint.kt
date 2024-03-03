package no.ibear.hendelse_produsent.entites

import java.time.Instant
import java.util.*

data class Datapoint(
    val id: UUID,
    val data: String,
    val created_at: Instant,
    val updated_at: Instant
)
