package com.jiangyt.library.toast.toast

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import com.jiangyt.library.toast.helper.NotificationServiceProxy
import java.lang.reflect.Proxy

/**
 * @Title: ktToast
 * @Package com.jiangyt.library.toast.toast
 * @Description: 处理Toast关闭通知栏权限后无法弹出的问题
 * @author jiangyt
 * @date 2022/4/12 11:38 上午
 * @version V1.0
 */
open class NotificationToast(application: Application) : SystemToast(application) {

    companion object {
        private var sHookService: Boolean = false

        @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
        @SuppressWarnings("JavaReflectionMemberAccess", "SoonBlockedPrivateApi")
        private fun hookNotificationService() {
            if (sHookService) {
                return
            }
            sHookService = true
            try {
                // 获取到 Toast 中的 getService 静态方法
                val getService = Toast::class.java.getDeclaredMethod("getService")
                getService.isAccessible = true
                // 执行方法，会返回一个 INotificationManager$Stub$Proxy 类型的对象
                val iNotificationManager = getService.invoke(null) ?: return
                // 如果这个对象已经被动态代理过了，并且已经 Hook 过了，则不需要重复 Hook
                if (Proxy.isProxyClass(iNotificationManager.javaClass) &&
                    Proxy.getInvocationHandler(iNotificationManager) is NotificationServiceProxy
                ) {
                    return
                }
                val iNotificationManagerProxy = Proxy.newProxyInstance(
                    Thread.currentThread().contextClassLoader,
                    arrayOf(Class.forName("android.app.INotificationManager")),
                    NotificationServiceProxy(iNotificationManager)
                )
                // 将原来的 INotificationManager$Stub$Proxy 替换掉
                val sService = Toast::class.java.getDeclaredField("sService")
                sService.isAccessible = true
                sService.set(null, iNotificationManagerProxy)
                //sService[null] = iNotificationManagerProxy
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun show() {
        hookNotificationService()
        super.show()
    }
}