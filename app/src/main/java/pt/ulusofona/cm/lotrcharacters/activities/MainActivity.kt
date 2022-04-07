package pt.ulusofona.cm.lotrcharacters.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.cm.lotrcharacters.databinding.ActivityMainBinding
import pt.ulusofona.cm.lotrcharacters.ui.viewModels.LOTRViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: LOTRViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    CoroutineScope(Dispatchers.Main).launch {
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