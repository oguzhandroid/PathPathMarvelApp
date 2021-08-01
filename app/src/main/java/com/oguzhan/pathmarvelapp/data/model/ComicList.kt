package com.oguzhan.pathmarvelapp.data.model

data class ComicList (
    var name : String,
    var year : Int
): Comparable<ComicList> {
    override fun compareTo(other: ComicList): Int {
        return COMPARATOR.compare(other,this)
    }
    companion object {
        private val COMPARATOR =
            Comparator.comparingInt<ComicList> { it.year }
    }

}