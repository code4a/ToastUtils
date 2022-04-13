package com.jiangyt.library.toast.style

import android.content.Context
import android.view.View
import com.jiangyt.library.toast.config.IToastStyle

/**
 * @Title: ktToast
 * @Package com.jiangyt.library.toast.style
 * @Description: Toast位置包装样式实现
 * @author jiangyt
 * @date 2022/4/12 9:53 上午
 * @version V1.0
 */
class LocationToastStyle(
    private val style: IToastStyle<*>,
    private val gravity: Int,
    private val xOffset: Int = 0,
    private val yOffset: Int = 0,
    private val horizontalMargin: Float = 0f,
    private val verticalMargin: Float = 0f
) : IToastStyle<View> {

    override fun createView(context: Context): View {
        return style.createView(context)
    }

    override fun getGravity(): Int {
        return gravity
    }

    override fun getXOffset(): Int {
        return xOffset
    }

    override fun getYOffset(): Int {
        return yOffset
    }

    override fun getHorizontalMargin(): Float {
        return horizontalMargin
    }

    override fun getVerticalMargin(): Float {
        return verticalMargin
    }
}