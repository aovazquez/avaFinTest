package com.mx.avafintest.utils

import android.content.Context
import android.text.InputFilter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mx.avafintest.ui.components.ProgressDialogFragment
import java.io.IOException
import java.io.InputStream

class Tools {

    companion object {
        private const val TAG: String = "Tools"

        var progressDialog: ProgressDialogFragment? = null
        private var activity: AppCompatActivity? = null
        private var PROGRESS_TAG: String = "PROGRESS"
        private var alertCustom: AlertDialog.Builder? = null

        /**
         * Function progressDialog
         * Displays a custom ProgressDialog with awesome animation :)
         * @param activity AppCompatActivity
         * @param cancelable Boolean
         * */
        fun progressDialog(cancelable: Boolean = true, activity: AppCompatActivity) {
            if (progressDialog == null) {
                progressDialog = ProgressDialogFragment()
                Companion.activity = activity
            }
            progressDialog!!.isCancelable = cancelable
            progressDialog!!.show(activity.supportFragmentManager, PROGRESS_TAG)
        }

        /**
         * Function dismissProgressDialog
         * Dismiss custom ProgressDialog already displayed
         * */
        fun dismissProgressDialog() {
            progressDialog?.let {
                it.dismiss()
                activity!!.supportFragmentManager.beginTransaction().remove(progressDialog!!).commit()
            }
            progressDialog = null
        }

        /**
         * Function showAlertMessage
         * Displays AlertDialog with custom title and message.
         * @param title String
         * @param message String
         * @param cancelable Boolean
         * @param cxt Context
         * */
        fun showAlertMessage(title: String, message: String, cancelable: Boolean = true, cxt: Context) {
            alertCustom = null
            alertCustom = AlertDialog.Builder(cxt).apply {
                setTitle(title)
                setMessage(message)
                setCancelable(cancelable)
                setPositiveButton("Aceptar") { _ , _ ->
                    alertCustom = null
                }
            }
            alertCustom!!.show()
        }

        /**
         * This function reads file in assets/
         * @param context
         * @param fileName With extension
         * **/
        fun readJSONFromAssets(context: Context, fileName: String): String? {
            return try {
                val inputStream: InputStream = context.assets.open(fileName)
                inputStream.bufferedReader().use { it.readText() }
            } catch (e: IOException) {
                null
            }
        }

        /**
         * This function adds a configuration to TextInputEditText (For manage name content)
         * @param field TextInputEditText
         */
        fun addFieldConfiguration(field: EditText, maxLenth: Int) {
            field.filters = arrayOf(
                getLengthFilter( maxLenth )
            )
        }

        /**
         * Method for GET InputFilter instance with custom FilterString
         * @param filterRegex String
         * @return InputFilter
         */
        fun getInputFilter(filterRegex:String) : InputFilter {
            return InputFilter { source, start, end, _, _, _ ->
                var i = start
                val stringBuilder = StringBuilder()
                while (i < end){
                    val checkMe = source[i].toString()
                    val rgx: Regex = filterRegex.toRegex()
                    if (rgx.matches(checkMe)){
                        stringBuilder.append(source[i])
                    }
                    i++
                }
                val allCharactersValid = (stringBuilder.length == end - start)
                return@InputFilter if (allCharactersValid) null else stringBuilder.toString()
            }
        }

        /**
         * Method for GET InputFilter instance with custom FilterRegexString and value to evaluate
         * @param value String
         * @param filterRegex String
         * @return Boolean
         */
        fun validateDataWithRegex(value: String, filterRegex:String): Boolean {
            val regex = filterRegex.toRegex()
            return  regex.matches(value)
        }

        /**
         * Method for GET InputFilter instance with maximum number of characters
         * @param max Int
         * @return InputFilter
         */
        fun getLengthFilter(max:Int) : InputFilter{
            return InputFilter.LengthFilter(max)
        }
    }
}