package com.jiangyt.library.toast

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.view.Gravity
import androidx.annotation.LayoutRes
import com.jiangyt.library.toast.config.IToastStrategy
import com.jiangyt.library.toast.config.IToastStyle
import com.jiangyt.library.toast.helper.ToastStrategy
import com.jiangyt.library.toast.style.BlackToastStyle
import com.jiangyt.library.toast.style.LocationToastStyle
import com.jiangyt.library.toast.style.ViewToastStyle

/**
 * @Title: ktToast
 * @Package com.jiangyt.library.toast
 * @Description: Toast工具类，
 *   copy from ： https://github.com/getActivity/ToastUtils
 *   显示的时间长短可以根据内容的长度自动转换
 * @author jiangyt
 * @date 2022/4/12 9:30 上午
 * @version V1.0
 */
object ToastUtils {

    /** Application 对象  */
    private lateinit var sApplication: Application

    /** Toast 处理策略  */
    private lateinit var sToastStrategy: IToastStrategy

    /** Toast 样式  */
    private lateinit var sToastStyle: IToastStyle<*>

    /** 调试模式  */
    private var sDebugMode: Boolean? = null

    /**
     * 初始化 Toast，需要在 Application.create 中初始化
     *
     * @param application       应用的上下文
     */
    fun init(application: Application) {
        init(application, BlackToastStyle())
    }

    fun init(application: Application, strategy: IToastStrategy?) {
        init(application, strategy, null)
    }

    fun init(application: Application, style: IToastStyle<*>?) {
        init(application, null, style)
    }

    /**
     * 初始化 Toast
     *
     * @param application       应用的上下文
     * @param strategy          Toast 策略
     * @param style             Toast 样式
     */
    fun init(application: Application, strategy: IToastStrategy?, style: IToastStyle<*>?) {
        var strategy: IToastStrategy? = strategy
        var style: IToastStyle<*>? = style
        sApplication = application

        // 初始化 Toast 策略
        if (strategy == null) {
            strategy = ToastStrategy()
        }
        setStrategy(strategy)

        // 设置 Toast 样式
        if (style == null) {
            style = BlackToastStyle()
        }
        setStyle(style)
    }

    /**
     * 判断当前框架是否已经初始化
     */
    fun isInit(): Boolean {
        return sApplication != null && sToastStrategy != null && sToastStyle != null
    }

    /**
     * 延迟显示 Toast
     */
    fun delayedShow(id: Int, delayMillis: Long) {
        show(id, delayMillis)
    }

    fun delayedShow(text: CharSequence?, delayMillis: Long) {
        show(text, delayMillis)
    }

    fun delayedShow(obj: Any?, delayMillis: Long) {
        show(obj, delayMillis)
    }

    /**
     * debug 模式下显示 Toast
     */
    fun debugShow(id: Int) {
        if (!isDebugMode()) {
            return
        }
        show(id, 0)
    }

    fun debugShow(text: CharSequence?) {
        if (!isDebugMode()) {
            return
        }
        show(text, 0)
    }

    fun debugShow(obj: Any?) {
        if (!isDebugMode()) {
            return
        }
        show(obj, 0)
    }

    /**
     * 显示默认样式的toast
     */
    fun showToast(text: CharSequence) {
        resetStyle()
        showCenter(text)
    }

    /**
     * 显示默认样式的toast
     * 适用于（R.string.xx）
     */
    fun showToast(resID: Int) {
        resetStyle()
        showCenter(resID)
    }

    /**
     * 在顶部显示默认样式的toast
     * 适用于（R.string.xx）
     */
    fun showTopToast(resID: Int) {
        resetStyle()
        showTop(resID)
    }

    /**
     * 在顶部显示默认样式 Toast
     */
    fun showTop(id: Int) {
        setTop()
        show(id)
    }

    /**
     * 重置为默认样式
     */
    fun resetStyle() {
        if (getStyle() is ViewToastStyle) {
            // 防止频繁创建style，只有替换过才恢复样式
            setStyle(BlackToastStyle())
        }
    }

    /**
     * 设置到顶部
     */
    private fun setTop() {
        if (getStrategy().getStyle().getGravity() != Gravity.TOP) {
            setGravity(Gravity.TOP)
        }
    }

    /**
     * 设置居中
     */
    private fun setCenter() {
        if (getStrategy().getStyle().getGravity() != Gravity.CENTER) {
            setGravity(Gravity.CENTER)
        }
    }

    /**
     * 在中心显示 Toast
     */
    fun showCenter(id: Int) {
        setCenter()
        show(id)
    }

    /**
     * 在中心显示 Toast
     */
    fun showCenter(text: CharSequence?) {
        setCenter()
        show(text)
    }

    /**
     * 显示 Toast
     */
    fun show(id: Int) {
        show(id, 0)
    }

    fun show(obj: Any?) {
        show(obj, 0)
    }

    fun show(text: CharSequence?) {
        show(text, 0)
    }

    private fun show(id: Int, delayMillis: Long) {
        try {
            // 如果这是一个资源 id
            show(sApplication.resources.getText(id), delayMillis)
        } catch (ignored: Resources.NotFoundException) {
            // 如果这是一个 int 整数
            show(id.toString(), delayMillis)
        }
    }

    private fun show(obj: Any?, delayMillis: Long) {
        show(obj?.toString() ?: "null", delayMillis)
    }

    private fun show(text: CharSequence?, delayMillis: Long) {
        // 如果是空对象或者空文本就不显示
        if (text.isNullOrEmpty()) {
            return
        }
        // 不需要拦截器
//        if (sToastInterceptor == null) {
//            sToastInterceptor = ToastLogInterceptor()
//        }
//        if (sToastInterceptor.intercept(text)) {
//            return
//        }
        sToastStrategy.showToast(text, delayMillis)
    }

    /**
     * 取消吐司的显示
     */
    fun cancel() {
        sToastStrategy.cancelToast()
    }

    /**
     * 设置吐司的位置
     *
     * @param gravity           重心
     */
    fun setGravity(gravity: Int) {
        setGravity(gravity, 0, 0)
    }

    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        setGravity(gravity, xOffset, yOffset, 0f, 0f)
    }

    fun setGravity(
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        horizontalMargin: Float,
        verticalMargin: Float
    ) {
        sToastStrategy.bindStyle(
            LocationToastStyle(
                sToastStyle,
                gravity,
                xOffset,
                yOffset,
                horizontalMargin,
                verticalMargin
            )
        )
    }

    /**
     * 给当前 Toast 设置新的布局
     */
    fun setView(@LayoutRes id: Int) {
        if (id <= 0) {
            return
        }
        setStyle(ViewToastStyle(id, sToastStyle))
    }

    /**
     * 初始化全局的 Toast 样式
     *
     * @param style         样式实现类，框架已经实现两种不同的样式
     * 黑色样式：[BlackToastStyle]
     */
    fun setStyle(style: IToastStyle<*>) {
        sToastStyle = style
        sToastStrategy.bindStyle(style)
    }

    fun getStyle(): IToastStyle<*> {
        return sToastStyle
    }

    /**
     * 设置 Toast 显示策略
     */
    fun setStrategy(strategy: IToastStrategy) {
        sToastStrategy = strategy
        sToastStrategy.registerStrategy(sApplication)
    }

    fun getStrategy(): IToastStrategy {
        return sToastStrategy
    }

    /**
     * 是否为调试模式
     */
    fun setDebugMode(debug: Boolean) {
        sDebugMode = debug
    }

    fun isDebugMode(): Boolean {
        if (sDebugMode == null) {
            sDebugMode = sApplication.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        }
        return sDebugMode!!
    }

}