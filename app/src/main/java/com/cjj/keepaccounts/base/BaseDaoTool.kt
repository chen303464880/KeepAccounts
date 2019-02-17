package com.cjj.keepaccounts.base

import android.arch.lifecycle.Lifecycle
import com.cjj.keepaccounts.listener.OnDaoChangeListener

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/8 10:16.
 */
abstract class BaseDaoTool<T> {
    val mOnChangeListeners = arrayListOf<Pair<OnDaoChangeListener<T>, LifecycleObserver?>>()

    fun addOnDaoChangeListener(listener: OnDaoChangeListener<T>, lifecycleObserver: LifecycleObserver?) {
        val pair = listener to lifecycleObserver
        mOnChangeListeners.add(pair)
        //添加注销事件,onDestroy时自动注销
        lifecycleObserver?.addLifecycleListener(Lifecycle.Event.ON_DESTROY) {
            removeOnDaoChangeListener(listener)
        }
    }

    fun removeOnDaoChangeListener(listener: OnDaoChangeListener<T>) {
        if (mOnChangeListeners.size > 0) {
            val size = mOnChangeListeners.size - 1
            for (i in size downTo 0) {
                if (mOnChangeListeners[i].first == listener) {
                    mOnChangeListeners.removeAt(i)
                    break
                }
            }
        }
    }

    protected fun notifyDaoInsertChange(entity: T) {
        mOnChangeListeners.forEach {
            it.second?.addLifecycleListener(Lifecycle.Event.ON_RESUME) {
                it.first.onInsertEntity(entity)
            } ?: it.first.onInsertEntity(entity)
        }

    }


    protected fun notifyDaoUpdateChange(oldEntity: T, newEntity: T) {
        mOnChangeListeners.forEach {
            it.second?.addLifecycleListener(Lifecycle.Event.ON_RESUME) {
                it.first.onUpdateEntity(oldEntity, newEntity)
            } ?: it.first.onUpdateEntity(oldEntity, newEntity)
        }

    }

    protected fun notifyDaoDeleteChange(entity: T) {
        mOnChangeListeners.forEach {
            it.second?.addLifecycleListener(Lifecycle.Event.ON_RESUME) {
                it.first.onDeleteEntity(entity)
            } ?: it.first.onDeleteEntity(entity)
        }

    }


    open fun insert(entity: T) {
        insert(entity, true)
    }

    open fun update(oldEntity: T, newEntity: T) {
        update(oldEntity, newEntity, true)
    }

    open fun delete(entity: T) {
        delete(entity, true)
    }

    abstract fun insert(entity: T, isNotify: Boolean)

    abstract fun update(oldEntity: T, newEntity: T, isNotify: Boolean)

    abstract fun delete(entity: T, isNotify: Boolean)

}