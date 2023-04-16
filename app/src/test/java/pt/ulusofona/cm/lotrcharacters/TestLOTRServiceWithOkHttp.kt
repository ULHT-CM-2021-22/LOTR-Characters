package pt.ulusofona.cm.lotrcharacters

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import pt.ulusofona.cm.lotrcharacters.data.remote.okHttp.LOTRServiceWithOkHttpAndGson
import pt.ulusofona.cm.lotrcharacters.data.remote.okHttp.LOTRServiceWithOkHttpAndJSONObject
import pt.ulusofona.cm.lotrcharacters.model.LOTRCharacter
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class TestLOTRServiceWithOkHttp: BaseMockWebserverTest() {

    @Test
    fun getCharactersWithGson() {

        server.enqueue(MockResponse().setBody(mockResponse))

        val client = OkHttpClient()
        val service = LOTRServiceWithOkHttpAndGson(server.url("").toString(), client)
        service.getCharacters {
            assertTrue(it.isSuccess)
            val characters = it.getOrNull()!!
            assertEquals(933, characters.size)
            assertEquals("Adanel", characters[0].name)
        }
    }

    /**
     * Since this is an async call, I create a countdown latch to properly
     * await for the execution
     */
    @Test
    fun getCharactersWithJSONObject() = runBlocking {

        server.enqueue(MockResponse().setBody(mockResponse))

        val latch = CountDownLatch(1)  // count starts with 1

        val client = OkHttpClient()
        val service = LOTRServiceWithOkHttpAndJSONObject(server.url("").toString(), client)

        val result = mutableMapOf<String,List<LOTRCharacter>>()
        service.getCharacters {
            assertTrue(it.isSuccess)
            result["list"] = it.getOrNull()!!
            latch.countDown()  // count--
        }

        // to wait for the response
        latch.await(1000, TimeUnit.MILLISECONDS) // suspends until count == 0

        assertEquals(933, result["list"]?.size)
        assertEquals("Adanel", result["list"]?.get(0)?.name)
    }



}