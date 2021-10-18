package com.optisol.optigeofencingandroid.utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.optisol.optigeofencingandroid.utils.TextUtils


object UiUtils {





    fun showToast(mActivity: AppCompatActivity, message: String) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun createAlertDialogWithTwoButtons(context: Context, title: String,
                                        message: String, buttonFirstText: String, buttonSecondText: String,
                                        firstListener: DialogInterface.OnClickListener,
                                        secondListener: DialogInterface.OnClickListener): AlertDialog {
        val alertDialog = AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonFirstText, firstListener)
                .setNegativeButton(buttonSecondText, secondListener)
                .create()
        if (TextUtils.isEmpty(title)) {
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        return alertDialog
    }

    fun createAlertDialog(context: Context, title: String, message: String,
                          buttonText: String, onClickListener: DialogInterface.OnClickListener): AlertDialog {
        val alertDialog = AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(buttonText, onClickListener)
                .create()
        if (TextUtils.isEmpty(title)) {
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        return alertDialog
    }
}
