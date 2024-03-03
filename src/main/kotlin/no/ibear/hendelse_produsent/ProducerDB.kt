package no.ibear.hendelse_produsent

import no.ibear.hendelse_produsent.entites.ChangeNotice
import no.ibear.hendelse_produsent.entites.Datapoint
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class ProducerDB(val jdbcTemplate: NamedParameterJdbcTemplate) {

    private val INSERT_DATAPOINT = "INSERT INTO datapoint(data) VALUES (:data)"
    private val UPDATE_DATAPOINT =  "UPDATE datapoint SET data = :data WHERE id = :id"
    private val SELECT_DATAPOINT_BY_ID = "SELECT * from datapoint where id=:id"

    private val SELECT_ALL_CHANGEFEED = "SELECT * from changefeed"
    private val SELECT_CHANGEFEED_AFTER_ID = "SELECT * from changefeed where id>:id"
    private val SELECT_CHANGEFEED_AFTER_TIMESTAMP = "SELECT * from changefeed where created_at>:timestamp"


    fun insertDataPoint(dataInput: String) {
        jdbcTemplate.update(INSERT_DATAPOINT, mapOf("data" to dataInput))
    }


    fun updateDataPint(id: UUID, dataInput: String) {
        jdbcTemplate.update(UPDATE_DATAPOINT, mapOf("id" to id, "data" to dataInput))
    }

    fun selectDataPoint(id: UUID): Datapoint {
        return jdbcTemplate.query(
            SELECT_DATAPOINT_BY_ID,
            mapOf("" to ""),
            DataClassRowMapper(Datapoint::class.java)
        ).first()
    }

    fun readAllChanges(): List<ChangeNotice> {
        return jdbcTemplate.query(
            SELECT_ALL_CHANGEFEED,
            DataClassRowMapper(ChangeNotice::class.java)
        )
    }

    fun readChangesAfterTime(lastRead: Instant): List<ChangeNotice>? {
        return jdbcTemplate.query(
            SELECT_CHANGEFEED_AFTER_TIMESTAMP,
            mapOf("timestamp" to lastRead),
            DataClassRowMapper(ChangeNotice::class.java))
    }

    fun readChangesAfterIndex(changeFeedID: Int): List<ChangeNotice> {
        return jdbcTemplate.query(
            SELECT_CHANGEFEED_AFTER_ID,
            mapOf("id" to changeFeedID),
            DataClassRowMapper(ChangeNotice::class.java)
        )
    }


}