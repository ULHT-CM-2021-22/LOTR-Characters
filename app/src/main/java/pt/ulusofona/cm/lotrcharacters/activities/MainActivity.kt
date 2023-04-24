package pt.ulusofona.cm.lotrcharacters.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.cm.lotrcharacters.databinding.ActivityMainBinding
import pt.ulusofona.cm.lotrcharacters.ui.viewModels.LOTRViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @VisibleForTesting
    lateinit var viewModel: LOTRViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LOTRViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        binding.getCharactersBtn.setOnClickListener {

            // show a circular progress indicator
            binding.getCharactersBtn.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                // call getCharacters on the "IO Thread"
                viewModel.getCharacters(
                    onSuccess =  { charactersUI ->
                            // process the result in the "Main Thread" since it will change the view
                            CoroutineScope(Dispatchers.Main).launch {
                                val intent = Intent(this@MainActivity, CharactersListActivity::class.java)
                                Log.i("APP", "Passing ${charactersUI.size} characters to CharactersListActivity")
                                intent.putParcelableArrayListExtra("characters", charactersUI)
                                startActivity(intent)

                                // dismiss the circular progress indicator
                                binding.getCharactersBtn.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.INVISIBLE
                            }
                    },
                    onFailure = {
                        CoroutineScope(Dispatchers.Main).launch {

                            // show a dialog
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("Error")
                                .setMessage("There was an error connecting to the server")
                                .setPositiveButton("OK") { _,_ -> }
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .create()
                                .show()

                            // dismiss the circular progress indicator
                            binding.getCharactersBtn.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.INVISIBLE
                        }
                    }
                )
            }
        }
    }
}