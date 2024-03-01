package no.ibear.hendelse

import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository


private const val INSERT_HENDELSE = "INSERT INTO hendelser(type, payload) VALUES (:type, :payload)"
private const val SELECT_ALL_HENDELSER = "SELECT * from hendelser"

@Repository
class PostgresDB(val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun insert(nyHendelse: NyHendelse) {
        jdbcTemplate.update(INSERT_HENDELSE, mapOf("type" to nyHendelse.type, "payload" to nyHendelse.payload))
    }

    fun listAll() : List<Hendelse>{

        return jdbcTemplate.query(
            SELECT_ALL_HENDELSER,
            DataClassRowMapper(Hendelse::class.java)
        )
    }
}