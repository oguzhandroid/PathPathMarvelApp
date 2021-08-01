package com.oguzhan.pathmarvelapp.ui.view.CharacterInfo.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.oguzhan.pathmarvelapp.R
import com.oguzhan.pathmarvelapp.data.model.ComicList

class ComicAdapter (private val context: Activity,private val comicList : ArrayList<ComicList>)
    : ArrayAdapter<ComicList>(context, R.layout.item_layout_comics,comicList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.item_layout_comics,null,true)

        val comicName = rowView.findViewById<TextView>(R.id.comic_name)
        val comicYear = rowView.findViewById<TextView>(R.id.comic_year)

        comicName.text = comicList[position].name
        comicYear.text = comicList[position].year.toString()
        return rowView
    }
}