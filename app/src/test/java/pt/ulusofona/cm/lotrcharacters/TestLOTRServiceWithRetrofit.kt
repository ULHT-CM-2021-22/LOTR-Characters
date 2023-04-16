package pt.ulusofona.cm.lotrcharacters

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import pt.ulusofona.cm.lotrcharacters.data.remote.retrofit.LOTRServiceWithRetrofit
import pt.ulusofona.cm.lotrcharacters.model.LOTRCharacter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class TestLOTRServiceWithRetrofit: BaseMockWebserverTest() {

    @Test
    fun getCharacters() = runBlocking {

        server.enqueue(MockResponse().setBody(mockResponse))

        val latch = CountDownLatch(1)  // count = 1

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = LOTRServiceWithRetrofit(retrofit)
        val result = mutableMapOf<String,List<LOTRCharacter>>()
        service.getCharacters {
            Assert.assertTrue(it.isSuccess)
            result["list"] = it.getOrNull()!!
            latch.countDown()  // count--
        }

        // to wait for the response
        latch.await(1000, TimeUnit.MILLISECONDS) // suspends until count == 0

        assertEquals(933, result["list"]?.size)
        assertEquals("Adanel", result["list"]?.get(0)?.name)
    }
}