package pt.ulusofona.cm.lotrcharacters

import okhttp3.OkHttpClient
import org.junit.Test

import org.junit.Assert.*
import pt.ulusofona.cm.lotrcharacters.data.remote.LOTRServiceWithOkHttp
import pt.ulusofona.cm.lotrcharacters.data.remote.LOTRServiceWithUrlConnection

class TestLOTRServiceWithOkHttp {
    @Test
    fun getCharacters() {

        val client = OkHttpClient()
        val service = LOTRServiceWithOkHttp(client)
        service.getCharacters {
            assertEquals(933, it.size)
            assertEquals("Adanel", it[0].name)
        }
    }
}