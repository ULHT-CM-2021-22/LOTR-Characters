package pt.ulusofona.cm.lotrcharacters.data.remote.retrofit

import com.google.gson.Gson
import okhttp3.Request
import okhttp3.ResponseBody
import pt.ulusofona.cm.lotrcharacters.LOTR_API_BASE_URL
import pt.ulusofona.cm.lotrcharacters.LOTR_API_TOKEN
import pt.ulusofona.cm.lotrcharacters.model.LOTRCharacter
import retrofit2.Retrofit

class LOTRServiceWithRetrofit(val retrofit: Retrofit) {

    fun getCharacters(onFinished: (List<LOTRCharacter>) -> Unit) {

        val service = retrofit.create(LOTRService::class.java)

        val response = service.getCharacters().execute()
        if (response.isSuccessful) {
            val responseObj = response.body()
            onFinished(responseObj?.docs?.map {
                LOTRCharacter(it._id, it.birth, it.death, it.gender.orEmpty(), it.name)
            }.orEmpty())
        } else {
            onFinished(emptyList())  // This should be handled with a onError() callback
        }
    }
}