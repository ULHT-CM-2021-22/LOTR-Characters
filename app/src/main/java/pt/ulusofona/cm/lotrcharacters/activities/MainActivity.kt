package pt.ulusofona.cm.lotrcharacters.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import pt.ulusofona.cm.lotrcharacters.R
import pt.ulusofona.cm.lotrcharacters.data.remote.LOTRServiceWithOkHttp
import pt.ulusofona.cm.lotrcharacters.databinding.ActivityMainBinding
import pt.ulusofona.cm.lotrcharacters.ui.viewModels.CharacterUI

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.getCharactersBtn.setOnClickListener {

//            val service = LOTRServiceWithUrlConnection()
            val service = LOTRServiceWithOkHttp(OkHttpClient())

            CoroutineScope(Dispatchers.IO).launch {
                service.getCharacters {
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

                    CoroutineScope(Dispatchers.Default).launch {
                        val intent = Intent(this@MainActivity, CharactersListActivity::class.java)
                        Log.i("APP", "Passing ${charactersUI.size} characters to CharactersListActivity")
                        intent.putParcelableArrayListExtra("characters", charactersUI)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}