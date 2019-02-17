package com.cjj.keepaccounts.listener

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/8 10:21.
 */
interface OnDaoChangeListener<in T> {
    fun onInsertEntity(entity: T)
    fun onUpdateEntity(oldEntity: T, newEntity: T)
    fun onDeleteEntity(entity: T)
    fun onRangeInsertEntity(entities: Iterable<T>) {}
    fun onRangeUpdateEntity(oldEntities: Iterable<T>, newEntities: Iterable<T>) {}
    fun onRangeDeleteEntity(entities: Iterable<T>) {}
}