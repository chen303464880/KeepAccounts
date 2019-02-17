package com.cjj.keepaccounts.bean.adapter

import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.utils.TimeUtils
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * @author CJJ
 * Created by CJJ on 2018/7/20 14:56.
 */
class RecordTypeAdapter : TypeAdapter<Record>() {

    override fun write(out: JsonWriter, value: Record) {
        out.beginObject()
        var recordTypeId = value.typeId
        if (recordTypeId in 1..100000L) {
            recordTypeId -= value.listId * 10000L
        } else if (recordTypeId > 100000L) {
            recordTypeId /= 10000L
        }
        out.name("account_id").value(value.accountId.toString())
        out.name("action_id").value(value.actionId.toString())
        out.name("list_id").value(value.listId.toString())
        out.name("content").value(value.content)
        out.name("ctime").value(value.createTime / 1000)
        out.name("credit_id").value(value.creditID.toString())
        out.name("is_impulse").value(value.isImpulse)
        out.name("money").value(value.money)
        out.name("notice_id").value(value.noticeId.toString())
        out.name("img").value(value.photo1)
        out.name("rate_money").value(value.rateMoney)
        out.name("rate_name").value(value.rateName)
        out.name("rtime").value(value.rTime / 1000)
        out.name("source").value("android 3.7.5")
        out.name("thedate").value(value.theDate)
        out.name("thehour").value(value.theHour)
        out.name("rt_id").value(recordTypeId.toString())
        out.name("device_key").value(value.deviceId)
        out.name("user_id").value(value.userId)
        out.name("is_deleted").value(value.isDeleted)
        out.name("mtime").value(value.mTime / 1000)
        out.name("uuid").value(value.uuid)
        out.endObject()

    }

    override fun read(reader: JsonReader): Record {
        reader.beginObject()
        val record = Record()
        record.source = ""
        record.photo1 = ""
        record.photo2 = ""
        record.photo3 = ""
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "list_id" -> {
                    record.listId = reader.nextLong()

                }
                "account_id" -> {
                    record.accountId = reader.nextLong()

                }
                "content" -> {
                    record.content = reader.nextString()

                }
                "ctime" -> {
                    record.createTime = reader.nextLong() * 1000

                }
                "money" -> {
                    record.money = reader.nextDouble()

                }
                "img" -> {
                    record.photo1 = reader.nextString()
                }
                "rtime" -> {
                    record.rTime = reader.nextLong() * 1000
                    record.day = TimeUtils.getDay(record.rTime)
                    record.month = TimeUtils.getMonth(record.rTime) - 1
                    record.year = TimeUtils.getYear(record.rTime)
                    record.theDate = TimeUtils.getTheDate(record.year, record.month, record.day)
                    record.theHour = TimeUtils.getHour(record.rTime)
                }
                "rt_id" -> {
                    record.typeId = reader.nextLong()

                }
                "device_key" -> {
                    record.deviceId = reader.nextString()
                }
                "is_deleted" -> {
                    record.isDeleted = reader.nextInt()
                }
                "mtime" -> {
                    record.setmTime(reader.nextLong() * 1000)
                }
                "uuid" -> {
                    record.uuid = reader.nextLong()
                }
                "user_id" -> {
                    record.userId = reader.nextString()
                }
                "notice_id" -> {
                    record.noticeId = reader.nextLong()
                }
                "action_id" -> {
                    record.actionId = reader.nextLong()
                }
                "rate_name" -> {
                    record.rateName = reader.nextString()
                }
                "rate_money" -> {
                    record.rateMoney = reader.nextDouble()
                }
                "is_impulse" -> {
                    record.isImpulse = reader.nextInt()
                }
                "credit_id" -> {
                    record.creditID = reader.nextLong()
                }
            }
        }
        reader.endObject()
        val temp = record.typeId
        if (temp in 1..10000L) {
            record.typeId = temp + record.listId * 10000L
        } else if (temp > 10000) {
            record.typeId = temp * 10000L
        }
        return record
    }
}