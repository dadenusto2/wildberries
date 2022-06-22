package com.example.week82.model

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {
    //интерфейс для
    @get:GET("all.json")
    val heroesData: Call<List<HeroData>>
}