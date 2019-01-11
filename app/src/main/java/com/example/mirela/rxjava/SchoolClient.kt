package com.example.mirela.rxjava

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

interface SchoolClient{

    @GET("schools.json")
    fun getSchools(): Single<Map<String, School>>


}
