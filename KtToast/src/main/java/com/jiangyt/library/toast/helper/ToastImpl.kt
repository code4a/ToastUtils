package com.jiangyt.library.toast.helper

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.WindowManager.BadTokenException
import android.widget.Toast
import com.jiangyt.library.toast.toast.NiuToast

/**
 * @Title: ktToast
 * @Package com.jiangyt.library.toast.helper
 * @Description: toast 自定义实现类
 * @author jiangyt
 * @date 2022/4/12 10:41 上午
 * @version V1.0
 */
class ToastImpl {

    companion object {
        private val HANDLER = Handler(Looper.getMainLooper())
    }

    /** 当前的吐司对象  */
    private lateinit var mToast: NiuToast

    /** WindowManager 辅助类  */
    private lateinit var mWindowLifecycle: WindowLifecycle

    /** 当前应用的包名  */
    private lateinit var mPackageName: String

    /** 当前是否全局显示  */
    private var mGlobalShow: Boolean = false

    /** 当前是否已经显示  */
    private var mShow = false

    constructor(activity: Activity, toast: NiuToast) : this(activity, toast, false) {
        mWindowLifecycle = WindowLifecycle(activity)
    }

    constructor(application: Application, toast: NiuToast) : this(application, toast, true) {
        mWindowLifecycle = WindowLifecycle(application)
    }

    private constructor(context: Context, toast: NiuToast, globalShow: Boolean) {
        mToast = toast
        mPackageName = context.packageName
        mGlobalShow = globalShow
    }

    fun isShow(): Boolean {
        return mShow
    }

    fun setShow(show: Boolean) {
        mShow = show
    }

    /***
     * 显示吐司弹窗
     */
    fun show() {
        if (isShow()) {
            return
        }
        if (isMainThread()) {
            mShowRunnable.run()
        } else {
            HANDLER.removeCallbacks(mShowRunnable)
            HANDLER.post(mShowRunnable)
        }
    }

    /**
     * 取消吐司弹窗
     */
    fun cancel() {
        if (!isShow()) {
            return
        }
        HANDLER.removeCallbacks(mShowRunnable)
        if (isMainThread()) {
            mCancelRunnable.run()
        } else {
            HANDLER.removeCallbacks(mCancelRunnable)
            HANDLER.post(mCancelRunnable)
        }
    }

    /**
     * 判断当前是否在主线程
     */
    private fun isMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }

    private val mShowRunnable = Runnable {
        val windowManager = mWindowLifecycle.getWindowManager()
        windowManager?.run {
            val params = WindowManager.LayoutParams()
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.format = PixelFormat.TRANSLUCENT
            params.flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            params.packageName = mPackageName
            params.gravity = mToast.getGravity()
            params.x = mToast.getXOffset()
            params.y = mToast.getYOffset()
            params.verticalMargin = mToast.getVerticalMargin()
            params.horizontalMargin = mToast.getHorizontalMargin()
            params.windowAnimations = mToast.getAnimationsId()

            // 如果是全局显示
            if (mGlobalShow) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                }
            }
            try {
                addView(mToast.getView(), params)
                // 添加一个移除吐司的任务
                HANDLER.postDelayed(
                    { cancel() },
                    (if (mToast.getDuration() == Toast.LENGTH_LONG) mToast.getLongDuration() else mToast.getShortDuration()).toLong()
                )
                // 注册生命周期管控
                mWindowLifecycle.register(this@ToastImpl)
                // 当前已经显示
                setShow(true)
            } catch (e: IllegalStateException) {
                // 如果这个 View 对象被重复添加到 WindowManager 则会抛出异常
                // java.lang.IllegalStateException: View android.widget.TextView has already been added to the window manager.
                // 如果 WindowManager 绑定的 Activity 已经销毁，则会抛出异常
                // android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@ef1ccb6 is not valid; is your activity running?
                e.printStackTrace()
            } catch (e: BadTokenException) {
                e.printStackTrace()
            }
        }
    }

    private val mCancelRunnable = Runnable {
        try {
            val windowManager = mWindowLifecycle.getWindowManager()
            windowManager?.removeViewImmediate(mToast.getView())
        } catch (e: IllegalArgumentException) {
            // 如果当前 WindowManager 没有附加这个 View 则会抛出异常
            // java.lang.IllegalArgumentException: View=android.widget.TextView not attached to window manager
            e.printStackTrace()
        } finally {
            // 反注册生命周期管控
            mWindowLifecycle.unregister()
            // 当前没有显示
            setShow(false)
        }
    }
}