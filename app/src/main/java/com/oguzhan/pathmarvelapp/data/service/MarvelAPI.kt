package com.oguzhan.pathmarvelapp.data.service

import com.oguzhan.pathmarvelapp.data.model.Characters
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelAPI {

    @GET("v1/public/characters?limit=30")
    fun getCharacters(
        @Query("hash") s: String,
        @Query("ts") ts: Long,
        @Query("offset") offset: Int
    ): Single<Characters>
}