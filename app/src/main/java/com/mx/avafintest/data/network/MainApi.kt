package com.mx.avafintest.data.network

import com.mx.avafintest.data.models.MainInfoData

interface MainApi {
    suspend fun getFields(): MainInfoData
}