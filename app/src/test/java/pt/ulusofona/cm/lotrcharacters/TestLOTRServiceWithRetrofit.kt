package pt.ulusofona.cm.lotrcharacters

import org.junit.Assert.assertEquals
import org.junit.Test
import pt.ulusofona.cm.lotrcharacters.data.remote.retrofit.LOTRServiceWithRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestLOTRServiceWithRetrofit {
    @Test
    fun getCharacters() {

        val retrofit = Retrofit.Builder()
            .baseUrl(LOTR_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = LOTRServiceWithRetrofit(retrofit)
        service.getCharacters {
            assertEquals(933, it.size)
            assertEquals("Adanel", it[0].name)
        }
    }
}