package pt.ulusofona.cm.lotrcharacters

import org.junit.Test

import org.junit.Assert.*
import pt.ulusofona.cm.lotrcharacters.data.remote.urlConnection.LOTRServiceWithUrlConnection

class TestLOTRServiceWithUrlConnection {

    @Test
    fun getCharacters() {

        val service = LOTRServiceWithUrlConnection()
        service.getCharacters {
            assertEquals(933, it.size)
            assertEquals("Adanel", it[0].name)
        }
    }
}