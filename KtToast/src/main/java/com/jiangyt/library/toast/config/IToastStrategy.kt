package com.jiangyt.library.toast.config

import android.app.Application
import com.jiangyt.library.toast.config.IToast

/**
 * @Title: ktToast
 * @Package com.jiangyt.library.toast.config
 * @Description: Toast处理策略
 * @author jiangyt
 * @date 2022/4/12 9:35 上午
 * @version V1.0
 */
interface IToastStrategy {
    /**
     * 注册策略
     */
    fun registerStrategy(application: Application)

    /**
     * 绑定样式
     */
    fun bindStyle(style: IToastStyle<*>)

    fun getStyle(): IToastStyle<*>

    /**
     * 创建 Toast
     */
    fun createToast(application: Application): IToast

    /**
     * 显示 Toast
     */
    fun showToast(text: CharSequence, delayMillis: Long)

    /**
     * 取消 Toast
     */
    fun cancelToast()
}