package com.cjj.keepaccounts.base

import android.os.Parcelable
import android.util.Log
import com.cjj.keepaccounts.utils.PresenterDelegate
import java.lang.ref.WeakReference

/**
 * @author CJJ
 * Created by CJJ on 2018/8/9 16:55.
 */
abstract class BasePresenter<V : IView, M : IModel>(view: V, protected val model: M) : LifecycleObserver() {
    private val weakReference: WeakReference<V> = WeakReference(view)
    protected val view: V = weakReference.get()!!


    open fun <T : Parcelable?> extra(key: String = "info"): PresenterDelegate<T> = PresenterDelegate(view, key)

    open fun presenter() {

    }

    override fun onCreate() {
        super.onCreate()
        Log.i("Lifecycle", view::class.java.name.substringAfterLast(".") + ":onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.i("Lifecycle", view::class.java.name.substringAfterLast(".") + ":onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("Lifecycle", view::class.java.name.substringAfterLast(".") + ":onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("Lifecycle", view::class.java.name.substringAfterLast(".") + ":onPause")
    }


    override fun onStop() {
        super.onStop()
        Log.i("Lifecycle", view::class.java.name.substringAfterLast(".") + ":onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        weakReference.clear()
        Log.i("Lifecycle", view::class.java.name.substringAfterLast(".") + ":onDestroy")
    }
}