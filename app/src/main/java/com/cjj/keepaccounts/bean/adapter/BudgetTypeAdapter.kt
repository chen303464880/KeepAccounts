package com.cjj.keepaccounts.bean.adapter

import com.cjj.keepaccounts.bean.Budget
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * @author CJJ
 * Created by CJJ on 2018/7/20 11:49.
 */
class BudgetTypeAdapter : TypeAdapter<Budget>() {
    override fun write(out: JsonWriter, value: Budget) {
        out.beginObject()
        out.name("amount").value(value.amount)
        out.name("ctime").value(value.cTime)
        out.name("list_id").value(value.listId.toString())
        out.name("name").value(value.name)
        out.name("type").value(value.type.toString())
        out.name("device_key").value(value.deviceId)
        out.name("user_id").value(value.userId)
        out.name("is_deleted").value(value.isDeleted)
        out.name("mtime").value((value.mTime / 1000).toString())
        out.name("uuid").value(value.uuid.toString())
        out.endObject()
    }

    override fun read(reader: JsonReader): Budget {
        reader.beginObject()
        val budget = Budget()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "user_id" -> {
                    budget.userId = reader.nextString()
                }
                "list_id" -> {
                    budget.listId = reader.nextLong()
                }
                "uuid" -> {
                    budget.uuid = reader.nextLong()
                }
                "type" -> {
                    budget.type = reader.nextInt()
                }
                "name" -> {
                    budget.name = reader.nextString()
                }
                "amount" -> {
                    budget.amount = reader.nextDouble()
                }
                "mtime" -> {
                    budget.mTime = reader.nextLong() * 1000
                }
                "ctime" -> {
                    budget.cTime = reader.nextLong()
                }
                "is_deleted" -> {
                    budget.isDeleted = reader.nextInt()
                }
            }
        }

        reader.endObject()
        return budget
    }
}