package no.ibear.hendelse_produsent

import no.ibear.hendelse_produsent.entites.ChangeNotice
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping(path = ["producer"])
class ProducerController(val db: ProducerDB) {

    @PostMapping("/data")
    fun add(@RequestParam dataInput: String): ResponseEntity<Unit> {
        db.insertDataPoint(dataInput)
        return ResponseEntity.status(201).build()
    }

    @PostMapping("/data/update")
    fun add(@RequestParam id: UUID, @RequestParam dataInput: String): ResponseEntity<Unit> {
        db.updateDataPint(id, dataInput)
        return ResponseEntity.status(200).build()
    }

    @GetMapping("/changefeed/all")
    fun allChanges(): ResponseEntity<List<ChangeNotice>> {
        return ResponseEntity.ok(db.readAllChanges())
    }
    @GetMapping("/changefeed/index")
    fun changesAfterIndex(@RequestParam lastReadIndex: Int): ResponseEntity<List<ChangeNotice>> {
        return ResponseEntity.ok(db.readChangesAfterIndex(lastReadIndex))
    }

    @GetMapping("/changefeed/time")
    fun changesAfterTime(@RequestParam(name = "lastReadTimeInEpoch") lastReadTimeInEpochMillis: Long): ResponseEntity<List<ChangeNotice>> {
        val instant = Instant.ofEpochMilli(lastReadTimeInEpochMillis)
        return ResponseEntity.ok(db.readChangesAfterTime(instant))
    }

}