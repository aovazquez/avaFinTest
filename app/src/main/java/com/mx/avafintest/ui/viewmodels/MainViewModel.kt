package com.mx.avafintest.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mx.avafintest.data.models.DataInfo
import com.mx.avafintest.data.models.FieldsData
import com.mx.avafintest.domain.GetFieldsDataUseCase
import com.mx.avafintest.ui.UiState
import com.mx.avafintest.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getFieldsDataUseCase: GetFieldsDataUseCase
) : ViewModel() {

    val getFieldsDataUiState = MutableLiveData<UiState<List<FieldsData>>>()


    /**
     * Method to request from the server all availables fields to create a dynamic form
     * and then return FieldsData List
     */
    fun getFieldsData() {
        viewModelScope.launch {
            getFieldsDataUiState.value = UiState.Loading
            val result = getFieldsDataUseCase()
            when ( result ) {
                is ResultWrapper.GenericError -> {
                    getFieldsDataUiState.value = UiState.Failure(result.errorBody?.message)
                }
                is ResultWrapper.NetworkError -> {
                    getFieldsDataUiState.value = UiState.Failure("Network Error")
                }
                is ResultWrapper.Success -> {
                    val orderList = convertObjectToArray( result.value!!.data )
                    getFieldsDataUiState.value = UiState.Success( orderList )
                }
            }
        }
    }

    /**
     * Method to convert Object response to List to create a dynamic form
     * @param fields DataInfo
     * @return List<FieldsData>
     */
    private fun convertObjectToArray(fields: DataInfo): List<FieldsData> {
        val list: MutableList<FieldsData> = mutableListOf()
        fields.customerLastname!!.name = "Last name"
        list.add(fields.customerLastname)
        fields.customerPhone!!.name = "Phone"
        list.add(fields.customerPhone)
        fields.customerMonthlyIncome!!.name = "Monthly income"
        list.add(fields.customerMonthlyIncome)
        fields.bankIbanta!!.name = "Banck Ibanta"
        list.add(fields.bankIbanta)
        fields.language!!.name = "Language"
        list.add(fields.language)
        fields.customerPersoncode!!.name = "Person Code"
        list.add(fields.customerPersoncode)
        fields.customerEmail!!.name = "Email"
        list.add(fields.customerEmail)
        fields.customerFirstname!!.name = "First name"
        list.add(fields.customerFirstname)
        fields.customerGender!!.name = "Gender"
        list.add(fields.customerGender)
        fields.customerBirthday!!.name = "Birthday"
        list.add(fields.customerBirthday)
        fields.pepStatus!!.name = "Status"
        list.add(fields.pepStatus)
        fields.amlCheck!!.name = "Check"
        list.add(fields.amlCheck)

        return list
            .filter { it.visible!! }
            .sortedBy { it.order }
    }
}