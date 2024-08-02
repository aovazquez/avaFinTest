package com.mx.avafintest.data.network

import com.mx.avafintest.data.models.MainInfoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainApiImplementation @Inject constructor(
    private val api: MainService
): MainApi {

    override suspend fun getFields(): MainInfoData = withContext(Dispatchers.IO) {
        api.getFields()
    }

}