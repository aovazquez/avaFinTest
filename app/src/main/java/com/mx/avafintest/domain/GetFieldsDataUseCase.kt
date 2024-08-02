package com.mx.avafintest.domain

import com.mx.avafintest.data.models.MainInfoData
import com.mx.avafintest.data.repository.MainRepository
import com.mx.avafintest.utils.ResultWrapper
import javax.inject.Inject

class GetFieldsDataUseCase @Inject constructor(private val repository: MainRepository) {
    suspend operator fun invoke(): ResultWrapper<MainInfoData?> {
        return repository.getFieldsData()
    }
}