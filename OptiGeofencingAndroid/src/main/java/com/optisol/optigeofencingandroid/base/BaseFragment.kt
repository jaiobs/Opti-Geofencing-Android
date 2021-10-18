package com.optisol.optigeofencingandroid.base

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment


open class BaseFragment : Fragment() {
    private var dialog: Dialog? = null
    private val REQUEST_RUNTIME_PERMISSION = 3265

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun hideSoftInput() {
        val manager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(requireActivity().window.currentFocus!!.windowToken, 0)
    }

    protected fun requestFocus(view: View) {
        if (view.requestFocus()) {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }


    fun getDataFromEdt(view: EditText): String {
        return view.text.toString()
    }

    fun getDataFromTxt(view: TextView): String {
        return view.text.toString()
    }

//    fun showProgress() {
//        hideProgress()
//        dialog = Dialog(requireActivity())
//        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog!!.setContentView(R.layout.progress_dialog)
//        dialog!!.setCancelable(false)
//        dialog!!.show()
//    }



    fun hideProgress() {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
    }

    override fun onPause() {
        super.onPause()
        hideProgress()
    }

    protected fun checkPermission(vararg M_PERMISSIONS: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var allPermissionsGranted = true
            var i = 0
            val mPermissionLength = M_PERMISSIONS.size
            while (i < mPermissionLength) {
                val permission = M_PERMISSIONS[i]
                if (ActivityCompat.checkSelfPermission(requireActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
                i++
            }
            if (!allPermissionsGranted) {
                requestPermissions(M_PERMISSIONS, REQUEST_RUNTIME_PERMISSION)
            } else {
                isPermissionGranted(true)
            }
        } else {
            isPermissionGranted(true)
        }
    }

    protected fun isPermissionGranted(isGranted: Boolean) {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("Req Code", "" + requestCode)
        if (requestCode == REQUEST_RUNTIME_PERMISSION) {
            var allPermissionGranted = true
            if (grantResults.size == permissions.size) {
                for (i in permissions.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        allPermissionGranted = false
                        break
                    }
                }
            } else {
                allPermissionGranted = false
            }
            if (allPermissionGranted) {

                isPermissionGranted(true)
            } else {
                isPermissionGranted(false)
            }
        }
    }



    fun setBundle(fragment: Fragment, bundle: Bundle): Fragment {
        fragment.arguments = bundle
        return fragment
    }
}
