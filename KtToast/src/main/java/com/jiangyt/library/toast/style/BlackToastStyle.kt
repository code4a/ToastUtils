package com.jiangyt.library.toast.style

import android.R
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.jiangyt.library.toast.config.IToastStyle

/**
 * @Title: ktToast
 * @Package com.jiangyt.library.toast.style
 * @Description: 默认黑色样式实现
 * @author jiangyt
 * @date 2022/4/12 9:41 上午
 * @version V1.0
 */
open class BlackToastStyle : IToastStyle<TextView> {

    override fun createView(context: Context): TextView {
        val textView = TextView(context)
        textView.id = R.id.message
        textView.gravity = getTextGravity(context)
        textView.setTextColor(getTextColor(context))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(context))

        val horizontalPadding = getHorizontalPadding(context)
        val verticalPadding = getVerticalPadding(context)

        // 适配布局反方向特性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setPaddingRelative(
                horizontalPadding,
                verticalPadding,
                horizontalPadding,
                verticalPadding
            )
        } else {
            textView.setPadding(
                horizontalPadding,
                verticalPadding,
                horizontalPadding,
                verticalPadding
            )
        }

        textView.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

        val background = getBackgroundDrawable(context)
        // 设置背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.background = background
        } else {
            textView.setBackgroundDrawable(background)
        }
        // 设置 Z 轴阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.z = getTranslationZ(context)
        }
        // 设置最大显示行数
        textView.maxLines = getMaxLines(context)
        return textView
    }

    protected open fun getTextGravity(context: Context?): Int {
        return Gravity.CENTER
    }

    protected open fun getTextColor(context: Context): Int {
        return Color.parseColor("#FFFFFFFF")
    }

    protected open fun getTextSize(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            14f,
            context.resources.displayMetrics
        )
    }

    protected open fun getHorizontalPadding(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            10f,
            context.resources.displayMetrics
        ).toInt()
    }

    protected open fun getVerticalPadding(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            10f,
            context.resources.displayMetrics
        ).toInt()
    }

    protected open fun getBackgroundDrawable(context: Context): Drawable? {
        val drawable = GradientDrawable()
        // 设置颜色
        drawable.setColor(Color.parseColor("#70000000"))
        // 设置圆角
        drawable.cornerRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            6f,
            context.resources.displayMetrics
        )
        return drawable
    }

    protected open fun getTranslationZ(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            3f,
            context.resources.displayMetrics
        )
    }

    protected open fun getMaxLines(context: Context): Int {
        return 5
    }
}