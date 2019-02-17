package com.cjj.keepaccounts.utils

import android.arch.lifecycle.Lifecycle
import com.cjj.keepaccounts.base.LifecycleObserver
import rx.Emitter
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 * @author CJJ
 * Created by CJJ on 2018/9/29 14:58.
 */

fun <T> Observable<T>.ioToMain(): Observable<T> = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.bindToLifecycle(lifecycle: LifecycleObserver): Observable<T> = bindToLifecycle(lifecycle, Lifecycle.Event.ON_DESTROY)


fun <T> Observable<T>.bindToLifecycle(lifecycle: LifecycleObserver, event: Lifecycle.Event): Observable<T> {
    return takeUntil(Observable.create<Emitter<Any>>({ t ->
        lifecycle.addLifecycleListener(event) {
            t?.onCompleted()
        }
    }, Emitter.BackpressureMode.BUFFER))

}

fun <T> Observable<T>.subscribeOnIo(): Observable<T> = subscribeOn(Schedulers.io())

fun <T> Observable<T>.observeOnMain(): Observable<T> = observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.subscribeMain(onNext: (t: T) -> Unit): Subscription = observeOnMain().subscribe { onNext.invoke(it) }
