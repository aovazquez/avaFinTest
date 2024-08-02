package com.mx.avafintest.core

import android.content.Context
import com.mx.avafintest.utils.Tools

object AlertMessages {

    fun showCustom(title:String, message: String, appContext: Context) {
        Tools.showAlertMessage(
            title = title,
            message = message,
            cxt = appContext,
        )
    }

}