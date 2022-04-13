package com.jiangyt.library.toast.helper

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager

/**
 * @Title: ktToast
 * @Package com.jiangyt.library.toast.helper
 * @Description: WindowManager生命周期监控
 * @author jiangyt
 * @date 2022/4/12 10:37 上午
 * @version V1.0
 */
class WindowLifecycle : Application.ActivityLifecycleCallbacks {

    /** 当前 Activity 对象  */
    private var mActivity: Activity? = null

    /** 当前 Application 对象  */
    private var mApplication: Application? = null

    /** 自定义 Toast 实现类  */
    private var mToastImpl: ToastImpl? = null

    constructor(activity: Activity) {
        mActivity = activity
    }

    constructor(application: Application) {
        mApplication = application
    }

    /**
     * 获取 WindowManager 对象
     */
    fun getWindowManager(): WindowManager? {
        if (mActivity != null) {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity!!.isDestroyed) {
                null
            } else mActivity!!.windowManager
        } else if (mApplication != null) {
            return mApplication!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
        return null
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    // A 跳转 B 页面的生命周期方法执行顺序：
    // onPause(A) ---> onCreate(B) ---> onStart(B) ---> onResume(B) ---> onStop(A) ---> onDestroyed(A)

    override fun onActivityPaused(activity: Activity) {
        if (mActivity != activity) {
            return
        }
        // 不能放在 onStop 或者 onDestroyed 方法中，因为此时新的 Activity 已经创建完成，必须在这个新的 Activity 未创建之前关闭这个 WindowManager
        // 调用取消显示会直接导致新的 Activity 的 onCreate 调用显示吐司可能显示不出来的问题，又或者有时候会立马显示然后立马消失的那种效果
        mToastImpl?.cancel()
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (mActivity != activity) {
            return
        }
        mToastImpl?.cancel()
        unregister()
        mActivity = null
    }


    /**
     * 注册
     */
    fun register(impl: ToastImpl) {
        mToastImpl = impl
        mActivity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                it.registerActivityLifecycleCallbacks(this)
            } else {
                it.application.registerActivityLifecycleCallbacks(this)
            }
        }

    }

    /**
     * 反注册
     */
    fun unregister() {
        mToastImpl = null
        mActivity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                it.unregisterActivityLifecycleCallbacks(this)
            } else {
                it.application.unregisterActivityLifecycleCallbacks(this)
            }
        }
    }
}