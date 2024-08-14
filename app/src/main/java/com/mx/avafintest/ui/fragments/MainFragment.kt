package com.mx.avafintest.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.mx.avafintest.R
import com.mx.avafintest.core.AlertMessages
import com.mx.avafintest.data.models.FieldsData
import com.mx.avafintest.databinding.FragmentMainBinding
import com.mx.avafintest.ui.UiState
import com.mx.avafintest.ui.components.CustomTextInputEditTextField
import com.mx.avafintest.ui.components.viewBinding
import com.mx.avafintest.utils.Tools
import com.mx.avafintest.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val bindingView by viewBinding(FragmentMainBinding::bind)

    private val mainViewModel : MainViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initListeners()

        mainViewModel.getFieldsData()
    }

    /**
     * method that initializes the listener to wait for the response from the server
     */
    private fun initListeners() {
        mainViewModel.getFieldsDataUiState.observe(viewLifecycleOwner) { state ->
            when ( state ) {
                is UiState.Failure -> {
                    dismissLoader()
                }
                is UiState.Loading -> {
                    showLoader()
                }
                is UiState.Success -> {
                    dismissLoader()
                    this.initLoadComponents( state.data )
                }
            }
        }
    }

    /**
     * Method to load the dynamic components with their particular validations for the main view
     * @param editTextList List<FieldsData>
     */
    private fun initLoadComponents(editTextList: List<FieldsData>) = with(bindingView) {

        val lastItem = editTextList.last()

        for (editText in editTextList) {
            val editTextCustom = CustomTextInputEditTextField(requireActivity())
            editTextCustom.fieldHolderStr = editText.name!!
            editTextCustom.fieldType = editText.type!!
            editTextCustom.fieldRegex = editText.regex
            editTextCustom.fieldMaxContent = editText.maxlength!!
            editTextCustom.fieldImeAction = "next"
            if ( editText == lastItem ) {
                editTextCustom.fieldImeAction = "done"
            }
            editTextCustom.setupStyle()
            llFormContainer.addView( editTextCustom )
        }

        val button = Button(requireActivity()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            text = context.getString(R.string.check_form)
            isAllCaps = false
            setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setOnClickListener {
                validateForm()
            }

        }
        llFormContainer.addView( button )
    }

    /**
     * Method for verifying that the form is correctly filled in
     */
    private fun validateForm() = with(bindingView) {
        var isValidForm = true
        for (i in 0 until llFormContainer.childCount) {
            val view: View = llFormContainer.getChildAt(i)
            when (view) {
                is CustomTextInputEditTextField -> {
                    if ( !view.isValidData() ){
                        isValidForm = false
                        break
                    }
                }
            }
        }

        if ( isValidForm ) {
            AlertMessages.showCustom("Congratulations","Success Form", requireActivity())
        } else {
            AlertMessages.showCustom("Warning","Wrong Form", requireActivity())
        }
    }

    private fun showLoader() {
        activity?.let {
            it.runOnUiThread {
                Tools.progressDialog(
                    cancelable = false,
                    activity = it as AppCompatActivity
                )
            }
        }
    }

    private fun dismissLoader() {
        Tools.dismissProgressDialog()
    }
}