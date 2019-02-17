package com.cjj.keepaccounts.base

import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.support.annotation.MainThread

/**
 * @author CJJ
 * Created by CJJ on 2018/9/28 11:32.
 */
abstract class LifecycleObserver : GenericLifecycleObserver {
    private var maps: HashMap<Lifecycle.Event, ArrayList<() -> Unit>>? = null

    var lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_CREATE

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        lifecycleEvent = event
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                onCreate()
            }
            Lifecycle.Event.ON_START -> {
                onStart()
            }
            Lifecycle.Event.ON_RESUME -> {
                onResume()
            }
            Lifecycle.Event.ON_PAUSE -> {
                onPause()
            }
            Lifecycle.Event.ON_STOP -> {
                onStop()
            }
            Lifecycle.Event.ON_DESTROY -> {
                onDestroy()
            }
            else -> {
                throw IllegalArgumentException("ON_ANY must not been send by anybody")
            }
        }
    }

    @MainThread
    fun addLifecycleListener(vararg events: Lifecycle.Event, runnable: () -> Unit) {
        if (maps == null) {
            maps = hashMapOf()
        }
        events.forEach {
            if (lifecycleEvent != it) {
                var list = maps!![it]
                if (list == null) {
                    list = arrayListOf()
                    maps!![it] = list
                }
                list.add(runnable)
            } else {
                runnable.invoke()
            }
        }
    }

    protected open fun onCreate() {
        runLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    protected open fun onStart() {
        runLifecycleEvent(Lifecycle.Event.ON_START)
    }

    protected open fun onResume() {
        runLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    protected open fun onPause() {
        runLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }


    protected open fun onStop() {
        runLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    protected open fun onDestroy() {
        runLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    private fun runLifecycleEvent(event: Lifecycle.Event) {
        if (maps != null) {
            val list = maps!![event]
            if (list != null && list.isNotEmpty()) {
                for (i in 0 until list.size) {
                    list[i].invoke()
                }
                list.clear()
            }
        }
    }
}