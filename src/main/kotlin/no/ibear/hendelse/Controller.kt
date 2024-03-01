package no.ibear.hendelse

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = [ "api" ])
class Controller (val db: PostgresDB){

    @GetMapping("/ping")
    fun ping() : ResponseEntity<String> {
        return ResponseEntity.ok("pong")
    }

    @GetMapping("/all")
    fun all() : ResponseEntity<List<Hendelse>> {
        return ResponseEntity.ok(db.listAll())
    }

    @GetMapping("/count")
    fun count() : ResponseEntity<String> {
        return ResponseEntity.ok("0")
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun lagreIDB(@RequestBody nyHendelse: NyHendelse) : ResponseEntity<Unit> {
        db.insert(nyHendelse)
        return ResponseEntity.status(201).build()
    }
}