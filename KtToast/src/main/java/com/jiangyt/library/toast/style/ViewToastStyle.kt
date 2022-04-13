package com.jiangyt.library.toast.style

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.jiangyt.library.toast.config.IToastStyle

/**
 * @Title: ktToast
 * @Package com.jiangyt.library.toast.style
 * @Description: Toast View 包装样式实现
 * @author jiangyt
 * @date 2022/4/12 10:11 上午
 * @version V1.0
 */
class ViewToastStyle(
    @LayoutRes private val mLayoutId: Int,
    private val style: IToastStyle<*>?,
) : IToastStyle<View> {

    override fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(mLayoutId, null)
    }

    override fun getGravity(): Int {
        return style?.getGravity() ?: Gravity.CENTER
    }

    override fun getXOffset(): Int {
        return style?.getXOffset() ?: 0
    }

    override fun getYOffset(): Int {
        return style?.getYOffset() ?: 0
    }

    override fun getHorizontalMargin(): Float {
        return style?.getHorizontalMargin() ?: 0f
    }

    override fun getVerticalMargin(): Float {
        return style?.getVerticalMargin() ?: 0f
    }
}