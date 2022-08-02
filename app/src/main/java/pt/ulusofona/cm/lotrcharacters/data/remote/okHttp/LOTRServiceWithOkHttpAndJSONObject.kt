package pt.ulusofona.cm.lotrcharacters.data.remote.okHttp

import com.google.gson.Gson
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

    override fun getCharacters(onFinished: (List<LOTRCharacter>) -> Unit) {

        val request: Request = Request.Builder()
            .url("$baseUrl/character")
            .addHeader("Authorization", "Bearer $LOTR_API_TOKEN")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val body = response.body?.string()
                    if (body != null) {
                        val jsonObject = JSONObject(body)
                        val jsonCharactersList = jsonObject["docs"] as JSONArray
                        val result = mutableListOf<LOTRCharacter>()
                        for (i in 0 until jsonCharactersList.length()) {
                            val jsonCharacter = jsonCharactersList[i] as JSONObject

                            result.add(
                                LOTRCharacter(
                                    jsonCharacter["_id"].toString(),
                                    jsonCharacter["birth"].toString(),
                                    jsonCharacter["death"].toString(),
                                    if (jsonCharacter.has("gender")) jsonCharacter["gender"].toString() else null,
                                    jsonCharacter["name"].toString()
                                )
                            )
                        }

                        onFinished(result)
                    }

                }
            }
        })
    }
}