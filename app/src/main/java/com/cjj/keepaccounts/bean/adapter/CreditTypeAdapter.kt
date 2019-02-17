package com.cjj.keepaccounts.bean.adapter

import com.cjj.keepaccounts.bean.Credit
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * @author CJJ
 * Created by CJJ on 2018/7/20 16:48.
 */
class CreditTypeAdapter : TypeAdapter<Credit>() {
    override fun write(out: JsonWriter, value: Credit) {
        out.beginObject()
        out.name("alertOffset").value(value.alertOffset)
        out.name("content").value(value.content)
        out.name("ctime").value((value.cTime / 1000).toString())
        out.name("isOpenRemind").value(value.isOpenRemind)
        out.name("money").value(value.money)
        out.name("dc_uname").value(value.dcUName)
        out.name("rate_name").value(value.rateName)
        out.name("settlementTime").value((value.settlementTime / 1000).toString())
        out.name("device_key").value(value.deviceId)
        out.name("user_id").value(value.userId)
        out.name("is_deleted").value(value.isDeleted)
        out.name("mtime").value((value.mTime / 1000).toString())
        out.name("uuid").value(value.uuid.toString())
        out.endObject()
    }

    override fun read(reader: JsonReader): Credit {
        val credit = Credit()
        credit.userId = ""
        credit.deviceId = ""
        reader.beginObject()

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "uuid" -> {
                    credit.uuid = reader.nextLong()
                }
                "ctime" -> {
                    credit.cTime = reader.nextLong() * 1000
                }
                "content" -> {
                    credit.content = reader.nextString()
                }
                "money" -> {
                    credit.money = reader.nextDouble()
                }
                "rate_name" -> {
                    credit.rateName = reader.nextString()
                }
                "is_deleted" -> {
                    credit.isDeleted = reader.nextInt()
                }
                "dc_uname" -> {
                    credit.dcUName = reader.nextString()
                }
                "mtime" -> {
                    credit.mTime = reader.nextLong() * 1000
                }
                "alertOffset" -> {
                    credit.alertOffset = reader.nextString()
                }
                "settlementTime" -> {
                    credit.settlementTime = reader.nextLong() * 1000
                }
                "isOpenRemind" -> {
                    credit.isOpenRemind = reader.nextInt()
                }
            }
        }
        reader.endObject()
        return credit

    }
}