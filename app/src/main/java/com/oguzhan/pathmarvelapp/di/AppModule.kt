package com.oguzhan.pathmarvelapp.di

import com.oguzhan.pathmarvelapp.data.service.MarvelAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    val BASE_URL = "https://gateway.marvel.com:443/"
    val API_KEY = "65caac27b5790806c42cfdca1100b1de"
    val PRIVATE_KEY = "cdf13a0060de0db3a5592bc0dcdb20f5ffd921e7"

    @Singleton
    @Provides
    fun provideOkHTTPClient() = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val originalHTTPUrl = original.url
            val url = originalHTTPUrl.newBuilder()
                .addQueryParameter("apikey", API_KEY)
                .build()

            val requestBuilder = original.newBuilder()
                .url(url)
            chain.proceed(requestBuilder.build())
        }
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit) =
        provideRetrofit(provideOkHTTPClient()).create(MarvelAPI::class.java)

}