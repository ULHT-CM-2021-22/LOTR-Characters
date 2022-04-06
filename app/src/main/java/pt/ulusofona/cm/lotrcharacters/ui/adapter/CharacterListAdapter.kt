package pt.ulusofona.cm.lotrcharacters.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.cm.lotrcharacters.databinding.ItemCharacterBinding
import pt.ulusofona.cm.lotrcharacters.ui.viewModels.CharacterUI

class CharacterListAdapter(val characters: List<CharacterUI>) :
    RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val item = characters[position]
        holder.binding.nameTv.text = item.name
    }

    override fun getItemCount() = characters.size
}