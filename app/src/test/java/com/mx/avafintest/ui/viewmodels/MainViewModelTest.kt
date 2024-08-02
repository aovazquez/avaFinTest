package com.mx.avafintest.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mx.avafintest.data.models.DataInfo
import com.mx.avafintest.data.models.FieldsData
import com.mx.avafintest.data.models.MainInfoData
import com.mx.avafintest.domain.GetFieldsDataUseCase
import com.mx.avafintest.ui.UiState
import com.mx.avafintest.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @RelaxedMockK
    private lateinit var getFieldsUseCase: GetFieldsDataUseCase

    private lateinit var mainViewModel: MainViewModel

    @get: Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        mainViewModel = MainViewModel( getFieldsUseCase )
        Dispatchers.setMain( Dispatchers.Unconfined )
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewmodels is created at the first time, get all fields and set all values`() = runTest {
        // Given
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

        val list: MutableList<FieldsData> = mutableListOf()
        list.add( fieldsData )

        coEvery { getFieldsUseCase() } returns ResultWrapper.Success(mainInfoData)

        //When
        mainViewModel.getFieldsData()

        //Then
        when (mainViewModel.getFieldsDataUiState.value) {
            is UiState.Failure -> {
                println("GenericError: error")
                throw AssertionError("Expected success but got error")
            }
            is UiState.Loading -> {
                println("UI Loading")
            }
            is UiState.Success -> {
                assert( (mainViewModel.getFieldsDataUiState.value as UiState.Success<List<FieldsData>>).data[0] == list.first() )
            }
            null -> {
                println("null data")
            }
        }
        // assert( mainViewModel. )
    }

}