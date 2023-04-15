package pt.ulusofona.cm.lotrcharacters.data.remote.retrofit

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.cm.lotrcharacters.model.LOTR
import pt.ulusofona.cm.lotrcharacters.model.LOTRCharacter
import retrofit2.Retrofit

class LOTRServiceWithRetrofit(val retrofit: Retrofit,
                              private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): LOTR() {

    override fun getCharacters(onFinished: (List<LOTRCharacter>) -> Unit,
                               onError: ((Exception) -> Unit)?,
                               onLoading: (() -> Unit)?) {

        CoroutineScope(ioDispatcher).launch {
            val service = retrofit.create(LOTRService::class.java)

            try {
                onLoading?.invoke()

                val responseObj: GetCharactersResponse = service.getCharacters()
                onFinished(responseObj.docs.map {
                    LOTRCharacter(it._id, it.birth, it.death, it.gender.orEmpty(), it.name)
                })
            } catch (e: Exception) {
                onError?.invoke(e)
            }
        }
    }
}