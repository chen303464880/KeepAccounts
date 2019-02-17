package com.cjj.keepaccounts.bean

import android.os.Parcel
import android.os.Parcelable
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.utils.Utils

/**
 * @author chenjunjie
 * Created by CJJ on 2018/1/24 15:17.
 */
data class ActivityInfoBean(var title: CharSequence, var back: CharSequence = Utils.getString(R.string.back), var titleColor: Int = Utils.getColor(R.color.AppThemeColor), var next: CharSequence? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title.toString())
        parcel.writeString(back.toString())
        parcel.writeInt(titleColor)
        parcel.writeString(next?.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActivityInfoBean> {
        override fun createFromParcel(parcel: Parcel): ActivityInfoBean {
            return ActivityInfoBean(parcel)
        }

        override fun newArray(size: Int): Array<ActivityInfoBean?> {
            return arrayOfNulls(size)
        }
    }


}