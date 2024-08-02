package com.mx.avafintest.data.network

import com.mx.avafintest.data.models.MainInfoData
import retrofit2.http.GET

interface MainService {

    @GET("getRegistrationfieldsResponse.json")
    suspend fun getFields(): MainInfoData
}