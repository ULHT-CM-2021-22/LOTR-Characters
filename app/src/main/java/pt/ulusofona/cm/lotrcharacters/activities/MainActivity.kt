package pt.ulusofona.cm.lotrcharacters.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import pt.ulusofona.cm.lotrcharacters.LOTR_API_BASE_URL
import pt.ulusofona.cm.lotrcharacters.R
import pt.ulusofona.cm.lotrcharacters.data.remote.okHttp.LOTRServiceWithOkHttp
import pt.ulusofona.cm.lotrcharacters.data.remote.retrofit.LOTRServiceWithRetrofit
import pt.ulusofona.cm.lotrcharacters.data.remote.retrofit.RetrofitBuilder
import pt.ulusofona.cm.lotrcharacters.databinding.ActivityMainBinding
import pt.ulusofona.cm.lotrcharacters.model.LOTR
import pt.ulusofona.cm.lotrcharacters.ui.viewModels.CharacterUI
import pt.ulusofona.cm.lotrcharacters.ui.viewModels.LOTRViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: LOTRViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LOTRViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        binding.getCharactersBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                // call getCharacters on the "IO Thread"
                viewModel.getCharacters { charactersUI ->
                    // process the result in the "Main Thread" since it will change the view
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