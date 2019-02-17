package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author CJJ
 * Created by CJJ on 2018/11/15 15:10.
 */
data class SelectColorInfo(var requestCode: Int, var resultCode: Int, var color: Int, var name: String, var idImg: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(requestCode)
        parcel.writeInt(resultCode)
        parcel.writeInt(color)
        parcel.writeString(name)
        parcel.writeInt(idImg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectColorInfo> {
        override fun createFromParcel(parcel: Parcel): SelectColorInfo {
            return SelectColorInfo(parcel)
        }

        override fun newArray(size: Int): Array<SelectColorInfo?> {
            return arrayOfNulls(size)
        }
    }
}