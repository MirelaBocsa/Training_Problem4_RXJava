package com.example.mirela.rxjava

import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchoolRepository {
    val apiService = NetworkModule.getRetrofitInstance().create(SchoolClient::class.java)

    var school: School? = null

    val schools: Single<List<School>>
        get() = apiService.getSchools().subscribeOn(Schedulers.newThread()).map { data ->
            val schools = data.values.sortedBy { it.district }.toMutableList()
            val demoSchool = schools.first { it.settingsCode == "demo" }
            schools.removeAll { it.settingsCode == "demo" || !it.enable }
            schools.add(demoSchool)
            return@map schools
        }
}


