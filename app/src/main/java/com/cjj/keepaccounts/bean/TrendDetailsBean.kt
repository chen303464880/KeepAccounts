package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author CJJ
 * Created by CJJ on 2018/5/15 13:40.
 */
data class TrendDetailsBean(val year: Int, val month: Int, var monthRecordInfo: MonthRecordInfo, var surplus: Double) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readParcelable(MonthRecordInfo::class.java.classLoader)!!,
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeParcelable(monthRecordInfo, flags)
        parcel.writeDouble(surplus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrendDetailsBean> {
        override fun createFromParcel(parcel: Parcel): TrendDetailsBean {
            return TrendDetailsBean(parcel)
        }

        override fun newArray(size: Int): Array<TrendDetailsBean?> {
            return arrayOfNulls(size)
        }
    }

}
