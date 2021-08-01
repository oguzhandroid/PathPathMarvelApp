package com.oguzhan.pathmarvelapp.ui.view.CharacterInfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.oguzhan.pathmarvelapp.R
import com.oguzhan.pathmarvelapp.data.model.ComicList
import com.oguzhan.pathmarvelapp.data.model.Results
import com.oguzhan.pathmarvelapp.ui.view.CharacterInfo.adapter.ComicAdapter
import com.oguzhan.pathmarvelapp.ui.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CharacterInfoFragment : DialogFragment() {

    lateinit var imgCharacter: ImageView
    lateinit var tvDescription: TextView
    lateinit var tvCharacterName: TextView
    lateinit var listView: ListView
    lateinit var toolbar: Toolbar

    lateinit var character: Results

    lateinit var comicList: ArrayList<ComicList>

    lateinit var comicAdapter: ComicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        character = MainActivity.currentCharacter!!
        initComics()
        initUI(view)
        initCharacter()
        initListView()
    }

    private fun initListView() {
        comicAdapter = ComicAdapter(requireActivity(), comicList)
        listView.adapter = comicAdapter

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initUI(rootView: View) {
        imgCharacter = rootView.findViewById(R.id.character_image)
        tvDescription = rootView.findViewById(R.id.tv_description)
        tvCharacterName = rootView.findViewById(R.id.tv_character_name)
        listView = rootView.findViewById(R.id.comics_list_view)
        toolbar = rootView.findViewById(R.id.toolbar)

        toolbar.title = character.name
        toolbar.navigationIcon = resources.getDrawable(R.drawable.back_icon)
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
    }

    private fun initComics() {
        comicList = arrayListOf()
        for (element in character.comics.items) {
            var counter = 0
            for (i in element.name.toCharArray()) {
                counter++
                if (i.toString() == "(") {
                    var comicYear = 0
                    var year = element.name.substring(counter, counter + 4)
                    try {
                        comicYear = Integer.parseInt(year)
                    } catch (e: Exception) {
                    }
                    if (comicYear != 0 && comicYear >= 2005 && comicList.size < 10) {
                        comicList.add(ComicList(element.name, comicYear))
                    }
                }
            }
        }
        comicList.sort()
    }

    private fun initCharacter() {
        tvCharacterName.text = character.name
        character.description?.let {
            if (it.isEmpty()) {
                tvDescription.visibility = View.GONE
            } else {
                tvDescription.text = character.description
            }
        }
        Glide.with(requireActivity())
            .load("${character.thumbnail.path}.${character.thumbnail.extension}")
            .into(imgCharacter)
    }

}