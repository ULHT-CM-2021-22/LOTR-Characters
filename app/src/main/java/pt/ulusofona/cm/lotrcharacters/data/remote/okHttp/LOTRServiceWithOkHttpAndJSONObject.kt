package pt.ulusofona.cm.lotrcharacters.data.remote.okHttp

import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import pt.ulusofona.cm.lotrcharacters.LOTR_API_BASE_URL
import pt.ulusofona.cm.lotrcharacters.LOTR_API_TOKEN
import pt.ulusofona.cm.lotrcharacters.model.LOTR
import pt.ulusofona.cm.lotrcharacters.model.LOTRCharacter
import java.io.IOException

class LOTRServiceWithOkHttpAndJSONObject(val baseUrl: String = LOTR_API_BASE_URL,
                                         val client: OkHttpClient) : LOTR() {

    override fun getCharacters(onFinished: (Result<List<LOTRCharacter>>) -> Unit) {

        val request: Request = Request.Builder()
            .url("$baseUrl/character")
            .addHeader("Authorization", "Bearer $LOTR_API_TOKEN")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFinished(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                response.apply {
                    if (!response.isSuccessful) {
                        onFinished(Result.failure(IOException("Unexpected code $response")))
                    } else {
                        val body = response.body?.string()
                        if (body != null) {
                            val jsonObject = JSONObject(body)
                            val jsonCharactersList = jsonObject["docs"] as JSONArray
                            val lotrCharacters = mutableListOf<LOTRCharacter>()
                            for (i in 0 until jsonCharactersList.length()) {
                                val jsonCharacter = jsonCharactersList[i] as JSONObject

                                lotrCharacters.add(
                                    LOTRCharacter(
                                        jsonCharacter["_id"].toString(),
                                        jsonCharacter["birth"].toString(),
                                        jsonCharacter["death"].toString(),
                                        if (jsonCharacter.has("gender")) jsonCharacter["gender"].toString() else null,
                                        jsonCharacter["name"].toString()
                                    )
                                )
                            }

                            onFinished(Result.success(lotrCharacters))
                        }
                    }

                }
            }
        })
    }
}