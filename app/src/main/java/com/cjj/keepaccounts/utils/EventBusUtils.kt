package com.cjj.keepaccounts.utils

import android.arch.lifecycle.Lifecycle
import com.cjj.keepaccounts.base.LifecycleObserver
import org.greenrobot.eventbus.EventBus

/**
 * @author CJJ
 * Created by CJJ on 2018/12/13 12:14.
 */
object EventBusUtils {
    @JvmStatic
    fun register(lifecycle: LifecycleObserver) {
        EventBus.getDefault().register(lifecycle)
        lifecycle.addLifecycleListener(Lifecycle.Event.ON_DESTROY) {
            EventBus.getDefault().unregister(lifecycle)
        }
    }

    @JvmStatic
    fun register(obj: Any, lifecycle: LifecycleObserver) {
        EventBus.getDefault().register(obj)
        lifecycle.addLifecycleListener(Lifecycle.Event.ON_DESTROY) {
            EventBus.getDefault().unregister(obj)
        }
    }
}
