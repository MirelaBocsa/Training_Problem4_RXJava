package com.example.mirela.rxjava.networking

import com.example.mirela.rxjava.School
import io.reactivex.Single
import retrofit2.http.GET

interface SchoolClient{

    @GET("schools.json")
    fun getSchools(): Single<Map<String, School>>


}
