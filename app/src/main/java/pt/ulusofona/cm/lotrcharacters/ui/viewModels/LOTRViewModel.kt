package pt.ulusofona.cm.lotrcharacters.ui.viewModels

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import pt.ulusofona.cm.lotrcharacters.LOTR_API_BASE_URL
import pt.ulusofona.cm.lotrcharacters.data.remote.okHttp.LOTRServiceWithOkHttpAndJSONObject
import pt.ulusofona.cm.lotrcharacters.model.LOTR

class LOTRViewModel: ViewModel() {

    @VisibleForTesting
    var model: LOTR =
        // comment/uncomment the desired method
//              LOTRServiceWithUrlConnection(LOTR_API_BASE_URL)
//              LOTRServiceWithOkHttpAndGson(LOTR_API_BASE_URL, OkHttpClient())
            LOTRServiceWithOkHttpAndJSONObject(LOTR_API_BASE_URL, OkHttpClient())
//        LOTRServiceWithRetrofit(RetrofitBuilder.getInstance(LOTR_API_BASE_URL))

    fun getCharacters(onSuccess: (ArrayList<CharacterUI>) -> Unit,
                      onFailure: (Throwable) -> Unit) {

        Log.i("APP", "ViewModel.getCharacters")

        // transforms "pure" LOTRCharacters into parcelable UICharacters
        model.getCharacters(
            onFinished = {
                when {
                    it.isSuccess -> {
                        val charactersFromModel = it.getOrNull()!!
                        Log.i("APP", "Received ${charactersFromModel.size} characters from WS")
                        val charactersUI = ArrayList(charactersFromModel.map { character ->
                            CharacterUI(
                                character.id,
                                character.birth,
                                character.death,
                                character.gender,
                                character.name
                            )
                        })
                        onSuccess(charactersUI)
                    }

                    it.isFailure -> {
                        Log.i("APP", "Error getting characters from WS: $it")
                        onFailure(it.exceptionOrNull()!!)
                    }
                }

            }
        )

    }
}