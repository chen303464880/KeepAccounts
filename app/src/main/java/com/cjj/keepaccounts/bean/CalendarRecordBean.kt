package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/21 14:12.
 */
data class CalendarRecordBean(val year: Int, val month: Int, val day: Int, var income: Double, var expend: Double, var isOverproof: Boolean
                              , var isChecked: Boolean, var isToday: Boolean, var isFuture: Boolean
                              , var isLastMonth: Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeInt(day)
        parcel.writeDouble(income)
        parcel.writeDouble(expend)
        parcel.writeByte(if (isOverproof) 1 else 0)
        parcel.writeByte(if (isChecked) 1 else 0)
        parcel.writeByte(if (isToday) 1 else 0)
        parcel.writeByte(if (isFuture) 1 else 0)
        parcel.writeByte(if (isLastMonth) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalendarRecordBean> {
        override fun createFromParcel(parcel: Parcel): CalendarRecordBean {
            return CalendarRecordBean(parcel)
        }

        override fun newArray(size: Int): Array<CalendarRecordBean?> {
            return arrayOfNulls(size)
        }
    }


}