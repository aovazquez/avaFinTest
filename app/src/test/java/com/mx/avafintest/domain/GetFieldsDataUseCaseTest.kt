package com.mx.avafintest.domain

import com.google.gson.Gson
import com.mx.avafintest.data.models.DataInfo
import com.mx.avafintest.data.models.FieldsData
import com.mx.avafintest.data.models.MainInfoData
import com.mx.avafintest.data.repository.MainRepository
import com.mx.avafintest.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetFieldsDataUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: MainRepository
    lateinit var getFieldsDataUseCase: GetFieldsDataUseCase
    private val gson = Gson()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getFieldsDataUseCase = GetFieldsDataUseCase( repository )
    }

    @Test
    fun `test FieldsData serialization and deserialization`() {
        val fieldsData = FieldsData(
            name = "Test Name",
            order = 1,
            visible = true,
            maxlength = 255,
            regex = "\\d+",
            type = "String"
        )
        val json = gson.toJson(fieldsData)
        val deserializedFieldsData = gson.fromJson(json, FieldsData::class.java)
        assert(fieldsData == deserializedFieldsData)
    }

    @Test
    fun `test DataInfo serialization and deserialization`() {
        val fieldsData = FieldsData(
            name = "Test Name",
            order = 1,
            visible = true,
            maxlength = 255,
            regex = "\\d+",
            type = "String"
        )

        val dataInfo = DataInfo(
            customerLastname = fieldsData,
            customerPhone = fieldsData,
            customerMonthlyIncome = fieldsData,
            bankIbanta = fieldsData,
            language = fieldsData,
            customerPersoncode = fieldsData,
            customerEmail = fieldsData,
            customerFirstname = fieldsData,
            customerGender = fieldsData,
            customerBirthday = fieldsData,
            pepStatus = fieldsData,
            amlCheck = fieldsData
        )

        val json = gson.toJson(dataInfo)
        val deserializedDataInfo = gson.fromJson(json, DataInfo::class.java)

        assert(dataInfo == deserializedDataInfo)
    }

    @Test
    fun `test MainInfoData serialization and deserialization`() {
        val fieldsData = FieldsData(
            name = "Test Name",
            order = 1,
            visible = true,
            maxlength = 255,
            regex = "\\d+",
            type = "String"
        )

        val dataInfo = DataInfo(
            customerLastname = fieldsData,
            customerPhone = fieldsData,
            customerMonthlyIncome = fieldsData,
            bankIbanta = fieldsData,
            language = fieldsData,
            customerPersoncode = fieldsData,
            customerEmail = fieldsData,
            customerFirstname = fieldsData,
            customerGender = fieldsData,
            customerBirthday = fieldsData,
            pepStatus = fieldsData,
            amlCheck = fieldsData
        )

        val mainInfoData = MainInfoData(
            ok = 1,
            data = dataInfo
        )

        val json = gson.toJson(mainInfoData)
        val deserializedMainInfoData = gson.fromJson(json, MainInfoData::class.java)

        assert(mainInfoData == deserializedMainInfoData)
    }

    @Test
    fun `when the api doesnt return anything then get error`() = runBlocking {
        // Given
        coEvery { repository.getFieldsData() } returns ResultWrapper.Success(MainInfoData(ok = 0, data = DataInfo()))

        //When
        val response = getFieldsDataUseCase()

        when (response) {
            is ResultWrapper.GenericError -> {
                println("GenericError: ${response.error} - ${response.code} - ${response.errorBody}")
                throw AssertionError("Expected success but got error")
            }
            is ResultWrapper.NetworkError -> {
                println("NetworkError: ${response.message}")
                throw AssertionError("Expected success but got error")
            }
            is ResultWrapper.Success -> {
                println("Success: ${response.value}")
                assert(response.value!!.data == DataInfo())
            }
        }

    }

    @Test
    fun `when the api return something then get values from api`() = runBlocking {

        val fieldsData = FieldsData(
            name = "Test Name",
            order = 1,
            visible = true,
            maxlength = 255,
            regex = "\\d+",
            type = "String"
        )
        val dataInfo = DataInfo(
            customerLastname = fieldsData,
            customerPhone = fieldsData,
            customerMonthlyIncome = fieldsData,
            bankIbanta = fieldsData,
            language = fieldsData,
            customerPersoncode = fieldsData,
            customerEmail = fieldsData,
            customerFirstname = fieldsData,
            customerGender = fieldsData,
            customerBirthday = fieldsData,
            pepStatus = fieldsData,
            amlCheck = fieldsData
        )

        val mainInfoData = MainInfoData(
            ok = 1,
            data = dataInfo
        )

        coEvery { repository.getFieldsData() } returns ResultWrapper.Success(mainInfoData)

        //When
        val response = getFieldsDataUseCase()

        when (response) {
            is ResultWrapper.GenericError -> {
                println("GenericError: ${response.error} - ${response.code} - ${response.errorBody}")
                throw AssertionError("Expected success but got error")
            }
            is ResultWrapper.NetworkError -> {
                println("NetworkError: ${response.message}")
                throw AssertionError("Expected success but got error")
            }
            is ResultWrapper.Success -> {
                println("Success: ${response.value}")
                assert(mainInfoData == response.value)
            }
        }
    }
}