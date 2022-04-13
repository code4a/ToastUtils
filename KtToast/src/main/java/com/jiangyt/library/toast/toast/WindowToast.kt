package com.jiangyt.library.toast.toast

import android.app.Activity
import android.app.Application
import com.jiangyt.library.toast.helper.ToastImpl

/**
 * @Title: ktToast
 * @Package com.jiangyt.library.toast.toast
 * @Description: 利用悬浮窗权限弹出全局 Toast
 * @author jiangyt
 * @date 2022/4/12 11:19 上午
 * @version V1.0
 */
class WindowToast(application: Application) : NiuToast() {

    private val mToastImpl: ToastImpl = ToastImpl(application, this)

    override fun show() {
        // 替换成 WindowManager 来显示
        mToastImpl.show()
    }

    override fun cancel() {
        // 取消 WindowManager 的显示
        mToastImpl.cancel()
    }
}