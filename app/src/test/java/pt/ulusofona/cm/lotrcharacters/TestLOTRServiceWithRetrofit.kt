package pt.ulusofona.cm.lotrcharacters

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pt.ulusofona.cm.lotrcharacters.data.remote.retrofit.LOTRServiceWithRetrofit
import pt.ulusofona.cm.lotrcharacters.model.LOTRCharacter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class TestLOTRServiceWithRetrofit {
    @Test
    fun getCharacters() = runBlocking {

        val latch = CountDownLatch(1)  // count = 1

        val retrofit = Retrofit.Builder()
            .baseUrl(LOTR_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = LOTRServiceWithRetrofit(retrofit)
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