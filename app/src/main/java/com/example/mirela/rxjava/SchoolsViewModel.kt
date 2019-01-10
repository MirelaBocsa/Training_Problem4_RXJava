package com.example.mirela.rxjava

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.annotations.BackpressureKind
import io.reactivex.annotations.BackpressureSupport
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Console


class SchoolsViewModel : ViewModel() {

    private val schoolRepository = SchoolRepository()

    val items: MutableLiveData<List<SchoolViewModel>> by lazy { MutableLiveData<List<SchoolViewModel>>() }

    val filter: ObservableField<String> by lazy { ObservableField<String>("") }

    private val progress: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val retry: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val clear: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private val refresh: PublishProcessor<Boolean> by lazy { PublishProcessor.create<Boolean>() }


    private val itemClick: (School) -> Unit = { school ->
        Log.e("item clicked ", school.district)
    }


    init {
        Flowables.combineLatest(schoolRepository.schools.toFlowable(), filter.toFlowable())
            .doOnSubscribe { progress.postValue(true) }
            .doOnNext { (schools, filter) ->
                progress.postValue(false)
                retry.postValue(false)
                val data = mutableListOf<SchoolViewModel>()
                if (filter.isEmpty()) {
                    data.addAll(schools.toMutableList().map { SchoolViewModel(it, itemClick) })
                } else {
                    val list = schools.filter { it.district.contains(filter, true) || it.state.contains(filter, true) }
                    data.addAll(list.map { SchoolViewModel(it, itemClick) })
                }
                items.value = data

                Log.e("items", items.value?.size.toString())
            }.doOnError {
                progress.postValue(false)
                retry.postValue(false)
            }.retryWhen { refresh }.subscribeBy()
        filter.toFlowable().subscribeBy { (clear.postValue(it.isNotEmpty())) }

//        val apiService = NetworkModule.getRetrofitInstance().create(SchoolClient::class.java)
//        val call = apiService.getSchools()
//
//        call.enqueue(object : Callback<MutableMap<String, School>> {
//            override fun onFailure(call: Call<MutableMap<String, School>>, t: Throwable) {
//                println(t.message)
//            }
//
//            override fun onResponse(
//                call: Call<MutableMap<String, School>>,
//                response: Response<MutableMap<String, School>>
//            ) {
//                if (response.isSuccessful) {
//                    val schools = response.body()!!.values
//                    val data = mutableListOf<SchoolViewModel>()
//                    data.addAll(schools.toMutableList().map { SchoolViewModel(it, itemClick) })
//
//                    Log.e("items ",data.size.toString())
//
//                    items.value=data
//
//                    Log.e("items mutable ", items.value?.size.toString())
//                } else
//                    println("ERRRRRRR")
//            }
//        })
    }


}


private val onNextStub: (Any) -> Unit = {}
private val onErrorStub: (Throwable) -> Unit = {}
private val onCompleteStub: () -> Unit = {}


private fun <T : Any> ((T) -> Unit).asConsumer(): Consumer<T> {
    return if (this === onNextStub) Functions.emptyConsumer() else Consumer(this)
}

private fun ((Throwable) -> Unit).asOnErrorConsumer(): Consumer<Throwable> {
    return if (this === onErrorStub) Functions.ON_ERROR_MISSING else Consumer(this)
}

private fun (() -> Unit).asOnCompleteAction(): Action {
    return if (this === onCompleteStub) Functions.EMPTY_ACTION else Action(this)
}

fun <T> ObservableField<T>.toFlowable(): Flowable<T> {
    val subject = BehaviorProcessor.create<T>()
    val callback = object : Observable.OnPropertyChangedCallback() {

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            Log.e("property change ","true")
            get()?.let {
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

@CheckReturnValue
@BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
@SchedulerSupport(SchedulerSupport.NONE)
fun <T : Any> Flowable<T>.subscribeBy(
    onError: (Throwable) -> Unit = onErrorStub,
    onComplete: () -> Unit = onCompleteStub,
    onNext: (T) -> Unit = onNextStub
): Disposable = subscribe(onNext.asConsumer(), onError.asOnErrorConsumer(), onComplete.asOnCompleteAction())