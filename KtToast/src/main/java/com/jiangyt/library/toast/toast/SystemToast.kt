package com.jiangyt.library.toast.toast

import android.app.Application
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.jiangyt.library.toast.config.IToast

/**
 * @Title: ktToast
 * @Package com.jiangyt.library.toast.toast
 * @Description: 系统toast
 * @author jiangyt
 * @date 2022/4/12 11:27 上午
 * @version V1.0
 */
@SuppressWarnings("deprecation")
open class SystemToast(application: Application) : Toast(application), IToast {

    /** 吐司消息 View  */
    private var mMessageView: TextView? = null

    override fun setView(view: View?) {
        super.setView(view)
        if (view == null) {
            mMessageView = null
            return
        }
        mMessageView = findMessageView(view)
    }

    override fun setText(text: CharSequence) {
        super.setText(text)
        mMessageView?.setText(text)
    }
}