package com.cjj.keepaccounts.bean.adapter

import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.manager.LogoManager
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * @author CJJ
 * Created by CJJ on 2018/7/20 11:49.
 */
class RecordTypeTypeAdapter : TypeAdapter<RecordType>() {
    override fun write(out: JsonWriter, value: RecordType) {
        out.beginObject()
        var uuid = value.uuid
        if (uuid in 1..100000L) {
            uuid -= value.listId * 10000L
        } else if (uuid > 100000L) {
            uuid /= 10000L
        }
        out.name("list_id").value(value.listId.toString())
        out.name("budget_amount").value(value.budgetAmount)
        out.name("budget_type").value(value.budgetType.toString())
        out.name("img").value(value.imgSrcId)
        out.name("income").value(value.isIncoming)
        out.name("theorder").value(value.orderIndex)
        out.name("content").value(value.typeDesc)
        out.name("type_id").value(uuid.toString())
        out.name("device_key").value(value.deviceId)
        out.name("user_id").value(value.userId)
        out.name("is_deleted").value(value.isDeleted)
        out.name("mtime").value((value.mTime / 1000).toString())
        out.name("uuid").value(uuid.toString())
        out.endObject()
    }

    override fun read(reader: JsonReader): RecordType {
        reader.beginObject()
        val recordType = RecordType()
        while (reader.hasNext()) {
            when (reader.nextName()) {

                "content" -> {
                    recordType.typeDesc = reader.nextString()
                }
                "income" -> {
                    recordType.isIncoming = reader.nextInt()
                }
                "img" -> {
                    recordType.imgSrcId = reader.nextInt()
                }
                "type_id" -> {
                    recordType.uuid = reader.nextLong()
                }
                "list_id" -> {
                    recordType.listId = reader.nextLong()
                }
                "theorder" -> {
                    recordType.orderIndex = reader.nextInt()
                }
                "device_key" -> {
                    recordType.deviceId = reader.nextString()
                }
                "user_id" -> {
                    recordType.userId = reader.nextString()
                }
                "is_deleted" -> {
                    recordType.isDeleted = reader.nextInt()
                }
                "mtime" -> {
                    recordType.mTime = reader.nextLong() * 1000
                }
                "budget_amount" -> {
                    recordType.budgetAmount = reader.nextString().toFloat()
                }
                "budget_type" -> {
                    recordType.budgetType = reader.nextLong()

                }
            }
        }
        reader.endObject()
        val logoInfo = LogoManager.Logo.logoSparse[recordType.imgSrcId]
        if (logoInfo != null) {
            recordType.color = logoInfo.color
        }
        val temp = recordType.uuid
        if (temp in 1..10000L) {
            recordType.uuid = temp + recordType.listId * 10000L
        } else if (temp > 10000) {
            recordType.uuid = temp * 10000L
        }
        return recordType
    }
}