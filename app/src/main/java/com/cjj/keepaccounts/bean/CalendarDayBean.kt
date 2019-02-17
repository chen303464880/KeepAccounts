package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/22 15:44.
 */
data class CalendarDayBean(val year: Int, val month: Int, val day: Int, var isChecked: Boolean, var isLastMonth: Boolean, var isNextMonth: Boolean
                           , var isFuture: Boolean, var isNode: Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeInt(day)
        parcel.writeByte(if (isChecked) 1 else 0)
        parcel.writeByte(if (isLastMonth) 1 else 0)
        parcel.writeByte(if (isNextMonth) 1 else 0)
        parcel.writeByte(if (isFuture) 1 else 0)
        parcel.writeByte(if (isNode) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalendarDayBean> {
        override fun createFromParcel(parcel: Parcel): CalendarDayBean {
            return CalendarDayBean(parcel)
        }

        override fun newArray(size: Int): Array<CalendarDayBean?> {
            return arrayOfNulls(size)
        }
    }

}