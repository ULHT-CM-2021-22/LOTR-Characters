package pt.ulusofona.cm.lotrcharacters.data.remote

import org.json.JSONArray
import org.json.JSONObject
import pt.ulusofona.cm.lotrcharacters.LOTR_API_BASE_URL
import pt.ulusofona.cm.lotrcharacters.LOTR_API_TOKEN
import pt.ulusofona.cm.lotrcharacters.model.LOTRCharacter
import java.net.URL

class LOTRServiceWithUrlConnection {

    fun getCharacters(onFinished: (List<LOTRCharacter>) -> Unit) {

        val url = URL("${LOTR_API_BASE_URL}/character")
        val connection = url.openConnection()
        connection.setRequestProperty("Authorization", "Bearer ${LOTR_API_TOKEN}")
        val response = connection.getInputStream().bufferedReader().readText()

        val jsonObject = JSONObject(response)
        val jsonCharactersList = jsonObject["docs"] as JSONArray
        val result = mutableListOf<LOTRCharacter>()
        for (i in 0 until jsonCharactersList.length()) {
            val jsonCharacter = jsonCharactersList[i] as JSONObject

            result.add(
                LOTRCharacter(
                    jsonCharacter["_id"].toString(),
                    jsonCharacter["birth"].toString(),
                    jsonCharacter["death"].toString(),
                    if (jsonCharacter.has("gender")) jsonCharacter["gender"].toString() else "Undefined",
                    jsonCharacter["name"].toString()
                )
            )

        }

        onFinished(result)
    }
}