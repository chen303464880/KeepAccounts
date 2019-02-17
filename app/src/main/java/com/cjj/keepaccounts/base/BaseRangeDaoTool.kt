package com.cjj.keepaccounts.base

import android.arch.lifecycle.Lifecycle

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/8 10:16.
 */
abstract class BaseRangeDaoTool<T> : BaseDaoTool<T>() {

    fun rangeInsert(entities: Iterable<T>) {
        rangeInsert(entities, true)
    }

    abstract fun rangeInsert(entities: Iterable<T>, isNotify: Boolean)

    fun rangeUpdate(oldEntities: Iterable<T>, newEntities: Iterable<T>) {
        rangeUpdate(oldEntities, newEntities, true)
    }

    abstract fun rangeUpdate(oldEntities: Iterable<T>, newEntities: Iterable<T>, isNotify: Boolean)

    fun rangeDelete(entities: Iterable<T>) {
        rangeDelete(entities, true)
    }

    abstract fun rangeDelete(entities: Iterable<T>, isNotify: Boolean)

    protected fun notifyRangeDaoInsertChange(entities: Iterable<T>) {

        mOnChangeListeners.forEach {
            it.second?.addLifecycleListener(Lifecycle.Event.ON_RESUME) {
                it.first.onRangeInsertEntity(entities)
            } ?: it.first.onRangeInsertEntity(entities)
        }

    }


    protected fun notifyRangeDaoUpdateChange(oldEntities: Iterable<T>, newEntities: Iterable<T>) {

        mOnChangeListeners.forEach {
            it.second?.addLifecycleListener(Lifecycle.Event.ON_RESUME) {
                it.first.onRangeUpdateEntity(oldEntities, newEntities)
            } ?: it.first.onRangeUpdateEntity(oldEntities, newEntities)
        }

    }

    protected fun notifyRangeDaoDeleteChange(entities: Iterable<T>) {

        mOnChangeListeners.forEach {
            it.second?.addLifecycleListener(Lifecycle.Event.ON_RESUME) {
                it.first.onRangeDeleteEntity(entities)
            } ?: it.first.onRangeDeleteEntity(entities)
        }

    }


}