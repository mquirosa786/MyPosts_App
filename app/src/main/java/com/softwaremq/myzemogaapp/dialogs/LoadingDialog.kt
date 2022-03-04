package com.softwaremq.myzemogaapp.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import com.softwaremq.myzemogaapp.databinding.DialogLoadingBinding


class LoadingDialog(context: Context?) : AlertDialog.Builder(context),
    DialogInterface {
    private var isCancelable = true
    private var isCanceledOnTouchOutside = true
    private var view: View? = null
    var binding: DialogLoadingBinding? = null
        private set
    private var alertDialog: AlertDialog? = null
    override fun create(): AlertDialog {
        super.setCancelable(isCancelable)
        if (view != null) binding = DataBindingUtil.bind(view!!)

        if (view != null) {
            super.setView(view)
        }
        alertDialog = super.create()
        if (alertDialog!!.window != null) alertDialog!!.window!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        alertDialog!!.setCanceledOnTouchOutside(false)
        alertDialog!!.setCancelable(false)
        return alertDialog!!
    }

    override fun show(): AlertDialog {
        return super.show()
    }

    override fun cancel() {
        if (alertDialog!!.isShowing) alertDialog!!.cancel()
    }

    override fun dismiss() {
        if (alertDialog == null) return
        if (alertDialog!!.isShowing) alertDialog!!.dismiss()
    }

    fun startLoadingAnimation(){
        binding?.ivLoading?.let {
            val rotate = RotateAnimation(360f,
                0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f)
            rotate.duration = 1000
            rotate.repeatCount = Animation.INFINITE
            rotate.interpolator = LinearInterpolator()
            it.startAnimation(rotate)
        }
    }

    val isShowing: Boolean
        get() = if (alertDialog != null) alertDialog!!.isShowing else false


    class Builder(private val context: Context) {
        private var mView: View? = null
        private var mBinding: DialogLoadingBinding? = null
        private var mIsCancelable = true
        private var mIsCanceledOnTouchOutside = true
        fun build(): LoadingDialog {
            val alert = LoadingDialog(context)
            alert.isCancelable = mIsCancelable
            alert.isCanceledOnTouchOutside = mIsCanceledOnTouchOutside

            alert.view = mView
            if (mView != null) mBinding = DataBindingUtil.bind(mView!!)
            mBinding?.let {
                alert.binding = it
                alert.startLoadingAnimation()
            }
            return alert
        }

        fun setView(view: View?): Builder {
            mView = view
            mBinding = DataBindingUtil.bind(mView!!)
            return this
        }

        fun setView(@LayoutRes idLayout: Int): Builder {
            if (context is Activity) mView = context.layoutInflater.inflate(idLayout, null)
            mBinding = DataBindingUtil.bind(mView!!)
            return this
        }

        fun setCancelable(isCancellable: Boolean): Builder {
            mIsCancelable = isCancellable
            return this
        }

        fun setCanceledOnTouchOutside(isCanceledOnTouchOutside: Boolean): Builder {
            mIsCanceledOnTouchOutside = isCanceledOnTouchOutside
            return this
        }
    }
}