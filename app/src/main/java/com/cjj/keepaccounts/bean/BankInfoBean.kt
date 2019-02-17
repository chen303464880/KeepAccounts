package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CJJ on 2018/4/10 21:13.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
data class BankInfoBean(val backName: String, val logoRes: Int, val color: Int, val nodeName: Char, val isNode: Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt().toChar(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(backName)
        parcel.writeInt(logoRes)
        parcel.writeInt(color)
        parcel.writeInt(nodeName.toInt())
        parcel.writeByte(if (isNode) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "BankInfoBean(backName='$backName', logoRes=$logoRes, color=$color, nodeName=$nodeName, isNode=$isNode)"
    }

    companion object CREATOR : Parcelable.Creator<BankInfoBean> {
        override fun createFromParcel(parcel: Parcel): BankInfoBean {
            return BankInfoBean(parcel)
        }

        override fun newArray(size: Int): Array<BankInfoBean?> {
            return arrayOfNulls(size)
        }
    }


}