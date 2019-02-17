package com.cjj.keepaccounts.enum

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CJJ on 2017/12/9 16:53.
 * Copyright © 2015-2017 CJJ All rights reserved.
 */
enum class InputType(val type: Int) : Parcelable {

    TEXT(0), MONEY(1);

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InputType> {
        override fun createFromParcel(parcel: Parcel): InputType {
            return InputType.values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<InputType?> {
            return arrayOfNulls(size)
        }
    }
}