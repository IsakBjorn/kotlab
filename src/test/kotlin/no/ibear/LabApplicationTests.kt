package no.ibear

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import org.springframework.web.client.toEntity
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class LabApplicationTests {


    @LocalServerPort
    lateinit var port: String

    companion object{
        @Container
        @ServiceConnection
        private val postgresql = PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
    }


    @Test
    fun contextLoads() {
    }

    @Test
    fun pingWorks(){

        val client = RestClient.create("http://localhost:$port")

        val response = client.get().uri("/api/ping").retrieve().toEntity<String>()

        expectThat(response.statusCode.value()).isEqualTo(200)
        expectThat(response.body).isEqualTo("pong")
    }

}
