package no.ibear.hendelse_produsent.entites

import java.time.Instant
import java.util.*

data class ChangeNotice(
    val id : Int,
    val datapoint_id : UUID,
    val created_at : Instant
)
