package com.example.mirela.rxjava

import io.reactivex.Single

class SchoolRepository {
    val apiService = NetworkModule.getRetrofitInstance().create(SchoolClient::class.java)

    val schools: Single<List<School>>
        get() = apiService.getSchools().map { data ->
            val schools = data.values.sortedBy { it.district }.toMutableList()
            val demoSchool = schools.first { it.settingsCode == "demo" }
            schools.removeAll { it.settingsCode == "demo" || !it.enable }
            schools.add(demoSchool)
            return@map schools
        }
}


