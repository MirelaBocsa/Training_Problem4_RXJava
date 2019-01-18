package com.example.mirela.rxjava.viewModel

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mirela.rxjava.School
import com.example.mirela.rxjava.SchoolRepository
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxkotlin.*


class SchoolsViewModel(schoolRepository: SchoolRepository) : ViewModel() {


    val items: MutableLiveData<List<SchoolViewModel>> by lazy { MutableLiveData<List<SchoolViewModel>>() }

    val filter: ObservableField<String> by lazy { ObservableField<String>("") }

    val moveNext: MutableLiveData<School> by lazy { MutableLiveData<School>() }

    private val itemClick: (School) -> Unit = { school ->
        schoolRepository.school = school
        moveNext.postValue(school)
    }


    init {
        Flowables.combineLatest(schoolRepository.schools.toFlowable(), filter.toFlowable())
            .doOnNext { (schools, filter) ->
                Log.e("do on next ", "enter")
                val data = mutableListOf<SchoolViewModel>()
                if (filter.isEmpty()) {
                    Log.e("filter ", "empty")
                    data.addAll(schools.asSequence().toMutableList().map {
                        SchoolViewModel(
                            it,
                            itemClick
                        )
                    }.toList())
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


class SchoolsViewModelFactory(private val schoolRepository: SchoolRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SchoolsViewModel::class.java)) {
            return SchoolsViewModel(schoolRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun <T> ObservableField<T>.toFlowable(): Flowable<T> {
    val subject = BehaviorProcessor.create<T>()
    val callback = object : Observable.OnPropertyChangedCallback() {

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
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