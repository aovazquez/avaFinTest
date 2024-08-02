package com.mx.avafintest.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FieldsData (
    @SerializedName("name") var name: String? = null,
    @SerializedName("order") val order: Int? = null,
    @SerializedName("visible") val visible: Boolean? = null,
    @SerializedName("maxlength") val maxlength: Int? = null,
    @SerializedName("regex") val regex: String? = null,
    @SerializedName("type") val type: String? = null
): Parcelable

@Parcelize
data class DataInfo (
    @SerializedName("customer-lastname") val customerLastname: FieldsData? = null,
    @SerializedName("customer-phone") val customerPhone: FieldsData? = null,
    @SerializedName("customer-monthly-income") val customerMonthlyIncome: FieldsData? = null,
    @SerializedName("bank-iban") val bankIbanta: FieldsData? = null,
    @SerializedName("language") val language: FieldsData? = null,
    @SerializedName("customer-personcode") val customerPersoncode: FieldsData? = null,
    @SerializedName("customer-email") val customerEmail: FieldsData? = null,
    @SerializedName("customer-firstname") val customerFirstname: FieldsData? = null,
    @SerializedName("customer-gender") val customerGender: FieldsData? = null,
    @SerializedName("customer-birthday") val customerBirthday: FieldsData? = null,
    @SerializedName("pep-status") val pepStatus: FieldsData? = null,
    @SerializedName("aml-check") val amlCheck: FieldsData? = null,

    ): Parcelable

@Parcelize
data class MainInfoData (
    @SerializedName("ok") val ok: Int,
    @SerializedName("data") val data: DataInfo
): Parcelable