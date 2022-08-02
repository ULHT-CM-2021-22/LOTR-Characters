package pt.ulusofona.cm.lotrcharacters

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import pt.ulusofona.cm.lotrcharacters.data.remote.okHttp.LOTRServiceWithOkHttpAndGson
import pt.ulusofona.cm.lotrcharacters.data.remote.okHttp.LOTRServiceWithOkHttpAndJSONObject
import pt.ulusofona.cm.lotrcharacters.model.LOTRCharacter
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class TestLOTRServiceWithOkHttp {

    @Test
    @Ignore
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
    @Ignore
    fun getCharactersWithJSONObject() = runBlocking {

        val latch = CountDownLatch(1)  // count starts with 1

        val client = OkHttpClient()
        val service = LOTRServiceWithOkHttpAndJSONObject(client)

        val result = mutableMapOf<String,List<LOTRCharacter>>()
        service.getCharacters {
            result["list"] = it
            latch.countDown()  // count--
        }

        // to wait for the response
        latch.await(1000, TimeUnit.MILLISECONDS) // suspends until count == 0

        assertEquals(933, result["list"]?.size)
        assertEquals("Adanel", result["list"]?.get(0)?.name)
    }

}