package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CJJ on 2018/5/7 20:45.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
data class MonthRecordInfo(var income:Double, var expend:Double) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(income)
        parcel.writeDouble(expend)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MonthRecordInfo> {
        override fun createFromParcel(parcel: Parcel): MonthRecordInfo {
            return MonthRecordInfo(parcel)
        }

        override fun newArray(size: Int): Array<MonthRecordInfo?> {
            return arrayOfNulls(size)
        }
    }
}