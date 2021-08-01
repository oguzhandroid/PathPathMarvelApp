package com.oguzhan.pathmarvelapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oguzhan.pathmarvelapp.data.service.MarvelAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class CharacterInfoViewModel @Inject constructor(
        val api: MarvelAPI
    ) : ViewModel() {
    private val disposable = CompositeDisposable()
    var error = MutableLiveData<Throwable>()
    var loading = MutableLiveData<Boolean>()

    fun getCharacter() {

    }
}