package pt.ulusofona.cm.lotrcharacters

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pt.ulusofona.cm.lotrcharacters.data.remote.retrofit.LOTRServiceWithRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CountDownLatch

class TestLOTRServiceWithRetrofit {
    @Test
    fun getCharacters() = runBlocking {

        val latch = CountDownLatch(1)

        val retrofit = Retrofit.Builder()
            .baseUrl(LOTR_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = LOTRServiceWithRetrofit(retrofit)
        service.getCharacters {
            assertEquals(933, it.size)
            assertEquals("Adanel", it[0].name)
            latch.countDown()
        }

        // to wait for the response
        latch.await()
    }
}