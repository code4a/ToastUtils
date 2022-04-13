package com.jiangyt.library.toast.helper

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * @Title: ktToast
 * @Package com.jiangyt.library.toast.helper
 * @Description: 通知服务代理代理对象
 * @author jiangyt
 * @date 2022/4/12 11:31 上午
 * @version V1.0
 */
class NotificationServiceProxy(val source: Any) : InvocationHandler {

    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
        when (method.name) {
            "enqueueToast", "enqueueToastEx", "cancelToast" -> {
                // 将包名修改成系统包名，这样就可以绕过系统的拦截
                // 部分华为机将 enqueueToast 修改成了 enqueueToastEx
                // 将包名修改成系统包名，这样就可以绕过系统的拦截
                // 部分华为机将 enqueueToast 修改成了 enqueueToastEx
                args[0] = "android"
            }
            else -> {}
        }
        // 使用动态代理
        return method.invoke(source, args)
    }
}