package com.softwaremq.myzemogaapp.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.softwaremq.myzemogaapp.R
import com.softwaremq.myzemogaapp.dialogs.LoadingDialog


open class BaseActivity: AppCompatActivity() {

    private var loadingDialog: LoadingDialog? = null

    fun goToFragment(f: Fragment, currentActivity: FragmentActivity){
        currentActivity.supportFragmentManager.beginTransaction().replace(R.id.container, f, Integer.toString(getFragmentCount()+1))
            .addToBackStack(null).commit()
    }

    fun getFragmentCount(): Int {
        return supportFragmentManager.backStackEntryCount
    }

    fun getFragmentAt(index: Int): Fragment? {
        return if (getFragmentCount() > 0) supportFragmentManager.findFragmentByTag(Integer.toString(
            index)) else null
    }

    fun setBackPressed(action:() -> Unit){
        action()
    }

    protected open fun getCurrentFragment(): Fragment? {
        return getFragmentAt(getFragmentCount())
    }

    protected open fun getPreviousFragment(): Fragment? {
        return getFragmentAt(getFragmentCount() - 1)
    }

    fun goToFragment(f: Fragment){
        goToFragment(f, this)
    }

    fun popBackStack(){
        supportFragmentManager.popBackStack()
    }


    fun clearAllFragmentStack() {
        val fm = this.supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }

    fun showLoadingDialog(){
        hideLoadingDialog()

        if (loadingDialog == null || !loadingDialog!!.isShowing) {

            loadingDialog =
                LoadingDialog.Builder(this)
                    .setView(R.layout.dialog_loading)
                    .build()
        }

        loadingDialog?.let {
            it.create()
            it.show()
        }
    }

    fun hideLoadingDialog(){
        loadingDialog?.let {
            if(it.isShowing){
                it.dismiss()
                loadingDialog = null
            }
        }
    }

}