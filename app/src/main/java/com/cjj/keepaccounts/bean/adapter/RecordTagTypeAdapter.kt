package com.cjj.keepaccounts.bean.adapter

import com.cjj.keepaccounts.bean.RecordTag
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * @author CJJ
 * Created by CJJ on 2018/7/20 15:45.
 */
class RecordTagTypeAdapter : TypeAdapter<RecordTag>() {
    override fun write(out: JsonWriter, value: RecordTag) {
        out.beginObject()
        out.name("list_id").value(value.listId.toString())
        out.name("ctime").value((value.createTime / 1000).toString())
        out.name("member_id").value(value.uuid.toString())
        out.name("member_name").value(value.tagName)
        out.name("device_key").value(value.deviceId)
        out.name("user_id").value(value.uuid)
        out.name("is_deleted").value(value.isDeleted)
        out.name("mtime").value((value.mTime / 1000).toString())
        out.name("uuid").value(value.uuid)
        out.endObject()
    }

    override fun read(reader: JsonReader): RecordTag {
        val recordTag = RecordTag()
        recordTag.deviceId = ""//TODO
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "member_id" -> {
                    recordTag.uuid = reader.nextLong()
                }
                "member_name" -> {
                    recordTag.tagName = reader.nextString()
                }
                "list_id" -> {
                    recordTag.listId = reader.nextLong()
                }
                "user_id" -> {
                    recordTag.userId = reader.nextString()
                }
                "is_deleted" -> {
                    recordTag.isDeleted = reader.nextInt()
                }
                "ctime" -> {
                    recordTag.createTime = reader.nextLong() * 1000
                }
                "mtime" -> {
                    recordTag.mTime = reader.nextLong() * 1000
                }
            }
        }
        reader.endObject()
        return recordTag
    }
}
