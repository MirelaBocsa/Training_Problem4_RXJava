package com.example.mirela.rxjava

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxkotlin.*


class SchoolsViewModel : ViewModel() {

    private val schoolRepository = SchoolRepository()

    val items: MutableLiveData<List<SchoolViewModel>> by lazy { MutableLiveData<List<SchoolViewModel>>() }

    val filter: ObservableField<String> by lazy { ObservableField<String>("") }

    private val itemClick: (School) -> Unit = { school ->
        Log.e("item clicked ", school.district)
    }


    init {
        Flowables.combineLatest(schoolRepository.schools.toFlowable(), filter.toFlowable())
            .doOnNext { (schools, filter) ->
                Log.e("do on next ", "enter")
                val data = mutableListOf<SchoolViewModel>()
                if (filter.isEmpty()) {
                    Log.e("filter ", "empty")
                    data.addAll(schools.asSequence().toMutableList().map { SchoolViewModel(it, itemClick) }.toList())
                    Log.e("filter ", data.size.toString())
                } else {
                    val list =
                        schools.filter { it.district.startsWith(filter, true) }
                    data.addAll(list.map { SchoolViewModel(it, itemClick) })
                }
                items.postValue(data)

                Log.e("items", items.value?.size.toString())
            }.doOnError {
                Log.e("do on error ", "enter")
            }.subscribe()


    }


}

fun <T> ObservableField<T>.toFlowable(): Flowable<T> {
    val subject = BehaviorProcessor.create<T>()
    val callback = object : Observable.OnPropertyChangedCallback() {

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            Log.d("property change ", "true")
            Log.d("property change ", "true")
            get()?.let {
                Log.d("property ", it.toString())
                subject.onNext(it)
            }
        }

    }
    get()?.let {
        subject.onNext(it)
    }
    return subject.doOnSubscribe { addOnPropertyChangedCallback(callback) }
        .doAfterTerminate { removeOnPropertyChangedCallback(callback) }
}