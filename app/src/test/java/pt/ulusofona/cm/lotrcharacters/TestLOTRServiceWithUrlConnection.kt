package pt.ulusofona.cm.lotrcharacters

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import pt.ulusofona.cm.lotrcharacters.data.remote.urlConnection.LOTRServiceWithUrlConnection
import java.io.File

class TestLOTRServiceWithUrlConnection {

    lateinit var server: MockWebServer

    @Before
    fun setupMockWebserver() {
        server = MockWebServer()
        server.start()
    }

    @Test
    fun getCharacters() {

        println("--->" + File(".").absolutePath)

        val mockResponse = File("src/test/res/characters.json").readText(Charsets.UTF_8)
        server.enqueue(MockResponse().setBody(mockResponse))

        // call the constructor with no args, if you want to connect with the real server
        val service = LOTRServiceWithUrlConnection(server.url("").toString())
        service.getCharacters {
            assertEquals(933, it.size)
            assertEquals("Adanel", it[0].name)
        }


    }

    @After
    fun shutdownMockWebserver() {
        server.shutdown();
    }
}