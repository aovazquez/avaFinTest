package com.mx.avafintest.data.repository

import android.content.Context
import com.google.gson.Gson
import com.mx.avafintest.data.models.MainInfoData
import com.mx.avafintest.utils.Tools
import com.mx.avafintest.utils.manageApiCall
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val gson: Gson,
    @ApplicationContext private val context: Context,
    private val dispatcher: CoroutineDispatcher
) {

    /**
     * Get json data from assets folder
     * */
    suspend fun getFieldsData() = manageApiCall(context, dispatcher) {
        Tools.readJSONFromAssets(context,"getRegistrationfieldsResponse.json")?.let {
            gson.fromJson(it, MainInfoData::class.java)
        }
    }

}