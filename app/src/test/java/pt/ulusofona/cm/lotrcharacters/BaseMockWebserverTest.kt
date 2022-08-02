package pt.ulusofona.cm.lotrcharacters

import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.io.File

abstract class BaseMockWebserverTest {

    lateinit var server: MockWebServer
    lateinit var mockResponse: String

    @Before
    fun setupMockWebserver() {
        server = MockWebServer()
        server.start()

        mockResponse = File("src/test/res/characters.json").readText(Charsets.UTF_8)
    }

    @After
    fun shutdownMockWebserver() {
        server.shutdown();
    }
}