package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author chenjunjie
 * Created by CJJ on 2017/12/7 15:01.
 */
@Suppress("MemberVisibilityCanPrivate")
data class ActivityJumpInfo(var requestCode: Int, var resultCode: Int, val inputType: Int, var backStr: String, var titleStr: String, var contentStr: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(requestCode)
        parcel.writeInt(resultCode)
        parcel.writeInt(inputType)
        parcel.writeString(backStr)
        parcel.writeString(titleStr)
        parcel.writeString(contentStr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActivityJumpInfo> {
        override fun createFromParcel(parcel: Parcel): ActivityJumpInfo {
            return ActivityJumpInfo(parcel)
        }

        override fun newArray(size: Int): Array<ActivityJumpInfo?> {
            return arrayOfNulls(size)
        }
    }


}