package com.example.mirela.rxjava.networking

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitFactory {
    companion object {
        const val BASE_URL = "https://launchpad-169908.firebaseio.com"
        val gson = GsonBuilder().setPrettyPrinting().create()

        fun getRetrofitInstance(): Retrofit {

            val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
            return Retrofit.Builder().baseUrl(BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
            addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build()


        }
    }
}
