package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/8 9:25.
 */
data class LogoInfo(val imgIndex: Int, val imgRes: Int, val color: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(imgIndex)
        parcel.writeInt(imgRes)
        parcel.writeInt(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LogoInfo> {
        override fun createFromParcel(parcel: Parcel): LogoInfo {
            return LogoInfo(parcel)
        }

        override fun newArray(size: Int): Array<LogoInfo?> {
            return arrayOfNulls(size)
        }
    }
}