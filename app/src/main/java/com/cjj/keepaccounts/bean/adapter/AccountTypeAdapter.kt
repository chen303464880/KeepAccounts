package com.cjj.keepaccounts.bean.adapter

import com.cjj.keepaccounts.bean.Account
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * @author CJJ
 * Created by CJJ on 2018/7/20 9:54.
 */
class AccountTypeAdapter : TypeAdapter<Account>() {
    override fun write(out: JsonWriter, value: Account) {
        out.beginObject()
        out.name("account_id").value(value.uuid.toString())
        out.name("bank_name").value(value.bankName)
        out.name("billDay").value(value.billDay.toString())
        out.name("color").value(if (value.color.length > 6) value.color.substring(value.color.length - 6, value.color.length) else value.color)
        out.name("content").value(value.desc)
        out.name("isHideAtInvestmentCenter").value(value.isShow)
        out.name("last_btime").value(value.lastBTime.toString())
        out.name("credit_limit").value(value.creditLimit)
        out.name("money").value(value.money)
        out.name("name").value(value.name)
        out.name("theorder").value(value.orderIndex.toString())
        out.name("returnDay").value(value.returnDay.toString())
        out.name("at_id").value(value.typeId.toString())
        out.name("device_key").value(value.deviceId)
        out.name("user_id").value(value.userId)
        out.name("is_deleted").value(value.isDeleted)
        out.name("mtime").value((value.mTime / 1000L).toString())
        out.name("uuid").value(value.uuid.toString())
        out.endObject()
    }

    override fun read(reader: JsonReader): Account {
        val account = Account()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "account_id" -> {
                    account.uuid = reader.nextLong()
                }
                "user_id" -> {
                    account.userId = reader.nextString()
                }
                "at_id" -> {
                    account.typeId = reader.nextInt()
                }
                "money" -> {
                    account.money = reader.nextDouble()
                }
                "content" -> {
                    account.desc = reader.nextString()
                }
                "name" -> {
                    account.name = reader.nextString()
                }
                "mtime" -> {
                    account.mTime = reader.nextLong() * 1000
                }
                "is_deleted" -> {
                    account.isDeleted = reader.nextInt()
                }
                "color" -> {
                    account.color = reader.nextString()
                }
                "device_key" -> {
                    account.deviceId = reader.nextString()
                }
                "theorder" -> {
                    account.orderIndex = reader.nextLong()
                }
                "last_btime" -> {
                    account.lastBTime = reader.nextInt()
                }
                "credit_limit" -> {
                    account.creditLimit = reader.nextString().toFloat()
                }
                "bank_name" -> {
                    account.bankName = reader.nextString()
                }
                "returnDay" -> {
                    account.returnDay = reader.nextInt()
                }
                "billDay" -> {
                    account.billDay = reader.nextInt()
                }
                "isHideAtInvestmentCenter" -> {
                    account.isShow = reader.nextInt()
                }
                else -> {

                }
            }
        }
        reader.endObject()
        return account
    }
}