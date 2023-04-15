package pt.ulusofona.cm.lotrcharacters.data.remote.okHttp

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import pt.ulusofona.cm.lotrcharacters.LOTR_API_BASE_URL
import pt.ulusofona.cm.lotrcharacters.LOTR_API_TOKEN
import pt.ulusofona.cm.lotrcharacters.model.LOTR
import pt.ulusofona.cm.lotrcharacters.model.LOTRCharacter
import java.io.IOException

class LOTRServiceWithOkHttpAndGson(val baseUrl: String = LOTR_API_BASE_URL, val client: OkHttpClient): LOTR() {

    override fun getCharacters(onFinished: (List<LOTRCharacter>) -> Unit,
                               onError: ((Exception) -> Unit)?,
                               onLoading: (() -> Unit)?) {

        // only used for parsing JSON response
        data class Character(val _id: String, val birth: String,
                             val death: String, val gender: String?, val name: String)

        // only used for parsing JSON response
        data class GetCharactersResponse(val docs: List<Character>, val total: Int)

        val request: Request = Request.Builder()
            .url("$baseUrl/character")
            .addHeader("Authorization", "Bearer $LOTR_API_TOKEN")
            .build()

        onLoading?.invoke()

        val response: ResponseBody? = client.newCall(request).execute().body
        if (response != null) {
            val responseObj = Gson().fromJson(response.string(), GetCharactersResponse::class.java)
            onFinished(responseObj.docs.map {
                LOTRCharacter(it._id, it.birth, it.death, it.gender.orEmpty(), it.name)
            })
        } else {
            onError?.invoke(IOException("response was null"))
        }
    }
}