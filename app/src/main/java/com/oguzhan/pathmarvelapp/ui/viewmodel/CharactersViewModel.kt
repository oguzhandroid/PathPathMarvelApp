package com.oguzhan.pathmarvelapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oguzhan.pathmarvelapp.data.model.Characters
import com.oguzhan.pathmarvelapp.data.service.MarvelAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor (
        val api : MarvelAPI
    ) : ViewModel(){
    private val disposable = CompositeDisposable()
    var characters = MutableLiveData<Characters>()
    var currentPage = MutableLiveData<Int>()
    var error = MutableLiveData<Throwable>()
    var loading = MutableLiveData<Boolean>()

    fun getCharacters(s : String,ts : Long,offset : Int) {

        loading.value = true
        disposable.add(
                api.getCharacters(s,ts,offset)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<Characters>() {
                            override fun onSuccess(t: Characters) {
                                characters.value = t
                                loading.value = false
                            }

                            override fun onError(e: Throwable) {
                                error.value = e
                                loading.value = false
                            }

                        })
        )
    }
}