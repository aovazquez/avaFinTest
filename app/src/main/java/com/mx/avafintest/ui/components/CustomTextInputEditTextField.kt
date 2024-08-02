package com.mx.avafintest.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import com.google.android.material.textfield.TextInputLayout
import com.mx.avafintest.databinding.ComponentTextInputEdittextBinding
import com.mx.avafintest.utils.Tools


class CustomTextInputEditTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_FIELD_TYPE = "text"
        private const val DEFAULT_IME_ACTION = "next"
        private const val DEFAULT_TEXT_MAX = 50
    }

    var fieldHolderStr: String = DEFAULT_FIELD_TYPE
    var fieldType : String = DEFAULT_FIELD_TYPE
    var fieldMaxContent : Int = DEFAULT_TEXT_MAX
    var fieldRegex : String? = null
    var fieldImeAction : String = DEFAULT_IME_ACTION


    private var binding: ComponentTextInputEdittextBinding = ComponentTextInputEdittextBinding.inflate(
        LayoutInflater.from(context), this, true)

    fun setupStyle() = with(binding) {

        tilMainField.hint = fieldHolderStr

        when(fieldType) {
            "text"-> {
                Tools.addFieldConfiguration(tieMainField, fieldMaxContent)
            }
        }

        when(fieldImeAction) {
            "done"->{
                tieMainField.imeOptions = EditorInfo.IME_ACTION_DONE
            }
            "search"->{
                tieMainField.imeOptions = EditorInfo.IME_ACTION_SEARCH
            }
            "next"->{
                tieMainField.imeOptions = EditorInfo.IME_ACTION_NEXT
            }
        }
    }

    /**
     * This function gets content of a TextInputEditText
     * @param field TextInputEditText
     * @return String?
     */
    fun getContent(): String? = with(binding){
        val content = tieMainField.text.toString().trim()
        if (content.isEmpty()) {
            return null
        }
        return content
    }

    fun isValidData(): Boolean = with(binding) {
        return Tools.validateDataWithRegex( tieMainField.text.toString().trim(), fieldRegex!! )
    }

}