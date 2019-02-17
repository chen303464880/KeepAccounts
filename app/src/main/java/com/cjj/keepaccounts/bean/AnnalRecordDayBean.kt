package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/26 14:40.
 */
data class AnnalRecordDayBean(var year: Int, var month: Int, var day: Int, var isLastMonth: Boolean, var state: Int, var isToday: Boolean, var isFuture: Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeInt(day)
        parcel.writeByte(if (isLastMonth) 1 else 0)
        parcel.writeInt(state)
        parcel.writeByte(if (isToday) 1 else 0)
        parcel.writeByte(if (isFuture) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AnnalRecordDayBean> {
        override fun createFromParcel(parcel: Parcel): AnnalRecordDayBean {
            return AnnalRecordDayBean(parcel)
        }

        override fun newArray(size: Int): Array<AnnalRecordDayBean?> {
            return arrayOfNulls(size)
        }
    }

}