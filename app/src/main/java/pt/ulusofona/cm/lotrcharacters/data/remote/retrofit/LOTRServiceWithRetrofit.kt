package pt.ulusofona.cm.lotrcharacters.data.remote.retrofit

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Request
import okhttp3.ResponseBody
import pt.ulusofona.cm.lotrcharacters.LOTR_API_BASE_URL
import pt.ulusofona.cm.lotrcharacters.LOTR_API_TOKEN
import pt.ulusofona.cm.lotrcharacters.model.LOTR
import pt.ulusofona.cm.lotrcharacters.model.LOTRCharacter
import retrofit2.HttpException
import retrofit2.Retrofit

class LOTRServiceWithRetrofit(val retrofit: Retrofit,
                              private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): LOTR() {

    override fun getCharacters(onFinished: (List<LOTRCharacter>) -> Unit) {

        CoroutineScope(ioDispatcher).launch {
            val service = retrofit.create(LOTRService::class.java)

            try {
                val responseObj: GetCharactersResponse = service.getCharacters()
                onFinished(responseObj.docs.map {
                    LOTRCharacter(it._id, it.birth, it.death, it.gender.orEmpty(), it.name)
                })
            } catch (e: HttpException) {
                onFinished(emptyList())  // This should be handled with a onError() callback
            }
        }
    }
}