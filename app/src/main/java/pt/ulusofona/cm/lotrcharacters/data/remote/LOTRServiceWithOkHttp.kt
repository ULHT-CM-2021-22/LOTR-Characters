package pt.ulusofona.cm.lotrcharacters.data.remote

import com.google.gson.Gson
import okhttp3.*
import pt.ulusofona.cm.lotrcharacters.LOTR_API_BASE_URL
import pt.ulusofona.cm.lotrcharacters.LOTR_API_TOKEN
import pt.ulusofona.cm.lotrcharacters.model.LOTRCharacter

class LOTRServiceWithOkHttp(val client: OkHttpClient) {

    fun getCharacters(onFinished: (List<LOTRCharacter>) -> Unit) {

        // only used for parsing JSON response
        data class Character(val _id: String, val birth: String,
                             val death: String, val gender: String?, val name: String)

        // only used for parsing JSON response
        data class GetCharactersResponse(val docs: List<Character>, val total: Int)

        val request: Request = Request.Builder()
            .url("${LOTR_API_BASE_URL}/character")
            .addHeader("Authorization", "Bearer ${LOTR_API_TOKEN}")
            .build()

        val response: ResponseBody? = client.newCall(request).execute().body
        if (response != null) {
            val responseObj = Gson().fromJson(response.string(), GetCharactersResponse::class.java)
            onFinished(responseObj.docs.map {
                LOTRCharacter(it._id, it.birth, it.death, it.gender.orEmpty(), it.name)
            })
        }
    }
}