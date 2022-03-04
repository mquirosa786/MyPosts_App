package com.softwaremq.myzemogaapp.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.softwaremq.myzemogaapp.base.BaseActivity
import com.softwaremq.myzemogaapp.posts.HomeFragment

open class BaseFragment:Fragment() {
    fun goToFragment(f: Fragment, currentActivity: FragmentActivity){
        (activity as BaseActivity).goToFragment(f, currentActivity)
    }

    fun goToFragment(f:Fragment) {
        (activity as BaseActivity).goToFragment(f)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackPressed { popBackStack() }
    }

    fun clearAllFragmentStack() = (activity as BaseActivity).clearAllFragmentStack()

    fun popBackStack() = (activity as BaseActivity).popBackStack()


    fun setBackPressed(action: () -> Unit){
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when(this@BaseFragment){
                        is HomeFragment -> requireActivity().finish()
                        else -> action()
                    }
                }
            }
            )
    }

    fun showLoadingDialog() = (activity as BaseActivity).showLoadingDialog()

    fun hideLoadingDialog() = (activity as BaseActivity).hideLoadingDialog()

}