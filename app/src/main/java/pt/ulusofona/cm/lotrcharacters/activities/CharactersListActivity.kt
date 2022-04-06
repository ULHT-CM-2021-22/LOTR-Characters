package pt.ulusofona.cm.lotrcharacters.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ulusofona.cm.lotrcharacters.R
import pt.ulusofona.cm.lotrcharacters.databinding.ActivityCharactersListBinding
import pt.ulusofona.cm.lotrcharacters.ui.adapter.CharacterListAdapter
import pt.ulusofona.cm.lotrcharacters.ui.viewModels.CharacterUI

class CharactersListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharactersListBinding

    private lateinit var characters: List<CharacterUI>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters_list)
        binding = ActivityCharactersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        characters = intent.getParcelableArrayListExtra("characters")!!

        binding.charactersListRv.layoutManager = LinearLayoutManager(this)
        binding.charactersListRv.adapter = CharacterListAdapter(characters)
        // add a divider between items of the list
        binding.charactersListRv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

    }
}