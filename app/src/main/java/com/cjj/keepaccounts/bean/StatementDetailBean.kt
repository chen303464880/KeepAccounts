package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author CJJ
 * Created by CJJ on 2018/5/14 11:51.
 */
data class StatementDetailBean(var startTime: Int, var endTime: Int, var uuid: Long,
                               var money: Double, var isIncoming: Int, var dsec: String,
                               var type: Int, val color: Int, val imgId: Int, var ratio: Double) : Parcelable, Comparable<StatementDetailBean> {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readLong(),
            parcel.readDouble(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(startTime)
        parcel.writeInt(endTime)
        parcel.writeLong(uuid)
        parcel.writeDouble(money)
        parcel.writeInt(isIncoming)
        parcel.writeString(dsec)
        parcel.writeInt(type)
        parcel.writeInt(color)
        parcel.writeInt(imgId)
        parcel.writeDouble(ratio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StatementDetailBean> {
        override fun createFromParcel(parcel: Parcel): StatementDetailBean {
            return StatementDetailBean(parcel)
        }

        override fun newArray(size: Int): Array<StatementDetailBean?> {
            return arrayOfNulls(size)
        }
    }

    override fun compareTo(other: StatementDetailBean): Int {
        return when {
            money > other.money -> -1
            money == other.money -> 0
            else -> 1
        }
    }
}