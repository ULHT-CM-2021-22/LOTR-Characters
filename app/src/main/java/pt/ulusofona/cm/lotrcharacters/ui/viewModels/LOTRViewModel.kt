package pt.ulusofona.cm.lotrcharacters.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import pt.ulusofona.cm.lotrcharacters.LOTR_API_BASE_URL
import pt.ulusofona.cm.lotrcharacters.data.remote.okHttp.LOTRServiceWithOkHttpAndGson
import pt.ulusofona.cm.lotrcharacters.data.remote.okHttp.LOTRServiceWithOkHttpAndJSONObject
import pt.ulusofona.cm.lotrcharacters.data.remote.retrofit.LOTRServiceWithRetrofit
import pt.ulusofona.cm.lotrcharacters.data.remote.retrofit.RetrofitBuilder
import pt.ulusofona.cm.lotrcharacters.model.LOTR

class LOTRViewModel: ViewModel() {

    private val model: LOTR =
        // comment/uncomment the desired method
//              LOTRServiceWithUrlConnection()
//              LOTRServiceWithOkHttpAndGson(OkHttpClient())
//              LOTRServiceWithOkHttpAndJSONObject(OkHttpClient())
        LOTRServiceWithRetrofit(RetrofitBuilder.getInstance(LOTR_API_BASE_URL))

    fun getCharacters(onFinished: (ArrayList<CharacterUI>) -> Unit) {

        // transforms "pure" LOTRCharacters into parcelable UICharacters
        model.getCharacters {
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
        }
    }
}