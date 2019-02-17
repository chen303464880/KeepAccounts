package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CJJ on 2018/5/28 20:31.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
data class DateIntervalBean(var startYear: Int, var startMonth: Int, var startDay: Int
                            , var endYear: Int, var endMonth: Int, var endDay: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(startYear)
        parcel.writeInt(startMonth)
        parcel.writeInt(startDay)
        parcel.writeInt(endYear)
        parcel.writeInt(endMonth)
        parcel.writeInt(endDay)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DateIntervalBean> {
        override fun createFromParcel(parcel: Parcel): DateIntervalBean {
            return DateIntervalBean(parcel)
        }

        override fun newArray(size: Int): Array<DateIntervalBean?> {
            return arrayOfNulls(size)
        }
    }
}