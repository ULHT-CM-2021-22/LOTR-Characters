package pt.ulusofona.cm.lotrcharacters.ui.viewModels

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import pt.ulusofona.cm.lotrcharacters.LOTR_API_BASE_URL
import pt.ulusofona.cm.lotrcharacters.data.remote.okHttp.LOTRServiceWithOkHttpAndGson
import pt.ulusofona.cm.lotrcharacters.data.remote.okHttp.LOTRServiceWithOkHttpAndJSONObject
import pt.ulusofona.cm.lotrcharacters.data.remote.retrofit.LOTRServiceWithRetrofit
import pt.ulusofona.cm.lotrcharacters.data.remote.retrofit.RetrofitBuilder
import pt.ulusofona.cm.lotrcharacters.data.remote.urlConnection.LOTRServiceWithUrlConnection
import pt.ulusofona.cm.lotrcharacters.model.LOTR

class LOTRViewModel: ViewModel() {

    @VisibleForTesting
    var model: LOTR =
        // comment/uncomment the desired method
//              LOTRServiceWithUrlConnection(LOTR_API_BASE_URL)
//              LOTRServiceWithOkHttpAndGson(LOTR_API_BASE_URL, OkHttpClient())
            LOTRServiceWithOkHttpAndJSONObject(LOTR_API_BASE_URL, OkHttpClient())
//        LOTRServiceWithRetrofit(RetrofitBuilder.getInstance(LOTR_API_BASE_URL))

    fun getCharacters(onFinished: (ArrayList<CharacterUI>) -> Unit,
                    onError: (Exception) -> Unit,
                    onLoading: () -> Unit) {

        Log.i("APP", "ViewModel.getCharacters")

        // transforms "pure" LOTRCharacters into parcelable UICharacters
        model.getCharacters(
            onFinished = {
                Log.i("APP", "Received ${it.size} characters from WS")
                val charactersUI = ArrayList(it.map { character ->
                    CharacterUI(
                        character.id,
                        character.birth,
                        character.death,
                        character.gender,
                        character.name
                    )
                })
                onFinished(charactersUI)
            },
            onError = {
                Log.i("APP", "Error getting characters from WS: $it")
                onError(it)
            },
            onLoading = {
                Log.i("APP", "Loading characters from WS...")
                onLoading()
            }
        )

    }
}