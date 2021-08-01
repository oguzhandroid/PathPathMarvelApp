package com.oguzhan.pathmarvelapp.ui.view.CharacterList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oguzhan.pathmarvelapp.R
import com.oguzhan.pathmarvelapp.data.model.Results

class CharacterAdapter(
    private val characterList: ArrayList<Results>
    , private val characterClickListener: CharacterClickListener
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    lateinit var context: Context

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.characters_name)
        val img: ImageView = view.findViewById(R.id.characters_img)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.carc_view_characters, parent, false)
        return CharacterViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {

        var data = characterList[position]

        holder.itemView.setOnClickListener {
            characterClickListener.onCharacterClickListener(data)
        }

        holder.name.text = characterList[position].name

        Glide
            .with(context)
            .load("${characterList[position].thumbnail.path}.${characterList[position].thumbnail.extension}")
            .into(holder.img)

    }

    fun addData(listItems: ArrayList<Results>) {
        var size = characterList.size
        characterList.addAll(listItems)
        var sizeNew = characterList.size
        notifyItemRangeChanged(size, sizeNew)
    }
}

interface CharacterClickListener {
    fun onCharacterClickListener(data: Results)
}