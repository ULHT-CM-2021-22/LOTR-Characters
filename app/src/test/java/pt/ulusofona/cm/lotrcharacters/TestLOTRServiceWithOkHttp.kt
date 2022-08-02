package pt.ulusofona.cm.lotrcharacters

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Test
import pt.ulusofona.cm.lotrcharacters.data.remote.okHttp.LOTRServiceWithOkHttpAndGson
import pt.ulusofona.cm.lotrcharacters.data.remote.okHttp.LOTRServiceWithOkHttpAndJSONObject
import java.util.concurrent.CountDownLatch

class TestLOTRServiceWithOkHttp {

    @Test
    fun getCharactersWithGson() {

        val client = OkHttpClient()
        val service = LOTRServiceWithOkHttpAndGson(client)
        service.getCharacters {
            assertEquals(933, it.size)
            assertEquals("Adanel", it[0].name)
        }
    }

    /**
     * Since this is an async call, I create a countdown latch to properly
     * await for the execution
     */
    @Test
    fun getCharactersWithJSONObject() = runBlocking {

        val latch = CountDownLatch(1)  // count starts with 1

        val client = OkHttpClient()
        val service = LOTRServiceWithOkHttpAndJSONObject(client)
        service.getCharacters {
            assertEquals(933, it.size)
            assertEquals("Adanel", it[0].name)
            latch.countDown()  // count--
        }

        // to wait for the response
        latch.await() // suspends until count == 0
    }

}