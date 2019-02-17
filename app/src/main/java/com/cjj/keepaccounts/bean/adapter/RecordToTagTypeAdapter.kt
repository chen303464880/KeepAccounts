package com.cjj.keepaccounts.bean.adapter

import com.cjj.keepaccounts.bean.RecordToTag
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * @author CJJ
 * Created by CJJ on 2018/7/20 15:45.
 */
class RecordToTagTypeAdapter : TypeAdapter<RecordToTag>() {
    override fun write(out: JsonWriter, value: RecordToTag) {
        out.beginObject()
        out.name("member_id").value(value.tagId.toString())
        out.name("record_id").value(value.recordId.toString())
        out.name("list_id").value(value.listId.toString())
        out.name("ctime").value(value.createTime.toString())
        out.name("device_key").value(value.deviceId)
        out.name("user_id").value(value.userId)
        out.name("is_deleted").value(value.isDeleted)
        out.name("mtime").value((value.mTime / 1000).toString())
        out.name("uuid").value(value.uuid.toString())
        out.endObject()
    }

    override fun read(reader: JsonReader): RecordToTag {
        val recordToTag = RecordToTag()
        recordToTag.deviceId //TODO
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "uuid" -> {
                    recordToTag.uuid = reader.nextLong()
                }
                "record_id" -> {
                    recordToTag.recordId = reader.nextLong()
                }
                "member_id" -> {
                    recordToTag.tagId = reader.nextLong()
                }
                "list_id" -> {
                    recordToTag.listId = reader.nextLong()
                }
                "user_id" -> {
                    recordToTag.userId = reader.nextLong().toString()
                }
                "is_deleted" -> {
                    recordToTag.isDeleted = reader.nextInt()
                }
                "ctime" -> {
                    recordToTag.createTime = reader.nextLong()
                }
                "mtime" -> {
                    recordToTag.mTime = reader.nextLong() * 1000
                }
            }
        }
        if (recordToTag.mTime == 0L) {
            recordToTag.mTime = recordToTag.uuid
        }
        reader.endObject()
        return recordToTag
    }
}