package com.jiangyt.library.toast.helper

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * @Title: ktToast
 * @Package com.jiangyt.library.toast.helper
 * @Description: activity生命周期监控
 * @author jiangyt
 * @date 2022/4/12 10:32 上午
 * @version V1.0
 */
class ActivityStack : Application.ActivityLifecycleCallbacks {

    companion object {
        /**
         * 注册
         */
        fun register(application: Application): ActivityStack {
            val lifecycle = ActivityStack()
            application.registerActivityLifecycleCallbacks(lifecycle)
            return lifecycle
        }
    }

    /** 前台 Activity 对象  */
    private var mForegroundActivity: Activity? = null

    fun getForegroundActivity(): Activity? {
        return mForegroundActivity
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        mForegroundActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        if (mForegroundActivity != activity) {
            return
        }
        mForegroundActivity = null
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}