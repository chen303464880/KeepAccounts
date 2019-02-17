package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/26 14:40.
 */
data class AnnalRecordMonthBean(var year: Int, var month: Int, var isNode: Boolean, var isFuture: Boolean, var days: List<AnnalRecordDayBean>?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.createTypedArrayList(AnnalRecordDayBean))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeByte(if (isNode) 1 else 0)
        parcel.writeByte(if (isFuture) 1 else 0)
        parcel.writeTypedList(days)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AnnalRecordMonthBean> {
        override fun createFromParcel(parcel: Parcel): AnnalRecordMonthBean {
            return AnnalRecordMonthBean(parcel)
        }

        override fun newArray(size: Int): Array<AnnalRecordMonthBean?> {
            return arrayOfNulls(size)
        }
    }


}