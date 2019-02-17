package com.cjj.keepaccounts.bean.adapter

import com.cjj.keepaccounts.bean.ListBook
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * @author CJJ
 * Created by CJJ on 2018/7/20 9:54.
 */
class ListBookTypeAdapter : TypeAdapter<ListBook>() {
    override fun write(out: JsonWriter, value: ListBook) {
        out.beginObject()

        out.name("rate_list").value(value.rateList)
        out.name("bg_img_mtime").value(value.bgImgMtime.toString())
        out.name("ctime").value((value.cTime / 1000).toString())
        out.name("date_start").value(value.dateStart)
        out.name("is_activated").value(value.isActivated)
        out.name("is_default").value(value.isDefault)
        out.name("list_bg_cimg").value(value.listBgCimg)
        out.name("list_bg_img").value(value.listBgImg)
        out.name("list_budget").value(value.listBudget)
        out.name("list_budget_ishide").value(value.listBudgetIshide)
        out.name("list_budget_ratename").value(value.listBudgetRatename)
        out.name("list_budget_show").value(value.listBudgetShow)
        out.name("list_color").value(value.listColor)
        out.name("list_cover").value(value.listCover)
        out.name("list_name").value(value.listName)
        out.name("list_order").value(value.listOrder)
        out.name("list_rate_budget").value(value.listRateBudget)
        out.name("list_text_color").value(value.listTextColor)
        out.name("list_type").value(value.listType.toString())
        out.name("unselectAccountIds").value(value.unselectAccountIds)
        out.name("rate_name").value(value.rateName)
        out.name("device_key").value(value.deviceId)
        out.name("user_id").value(value.userId)
        out.name("is_deleted").value(value.isDeleted)
        out.name("mtime").value((value.mTime / 1000).toString())
        out.name("uuid").value(value.uuid.toString())

        out.endObject()
    }

    override fun read(reader: JsonReader): ListBook {
        val listBook = ListBook()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "uuid" -> {
                    listBook.uuid = reader.nextLong()
                }
                "user_id" -> {
                    listBook.userId = reader.nextString()
                }
                "ctime" -> {
                    listBook.cTime = reader.nextLong() * 1000
                }
                "mtime" -> {
                    listBook.mTime = reader.nextLong() * 1000
                }
                "is_deleted" -> {
                    listBook.isDeleted = reader.nextInt()
                }
                "is_activated" -> {
                    listBook.isActivated = reader.nextInt()
                }
                "is_default" -> {
                    listBook.isDeleted = reader.nextInt()
                }
                "list_name" -> {
                    listBook.listName = reader.nextString()
                }
                "list_color" -> {
                    listBook.listColor = reader.nextString()
                }
                "list_order" -> {
                    listBook.listOrder = reader.nextInt()
                }
                "list_type" -> {
                    listBook.listType = reader.nextLong()
                }
                "list_cover" -> {
                    listBook.listCover = reader.nextString()
                }
                "list_bg_img" -> {
                    listBook.listBgImg = reader.nextString()
                }
                "list_bg_cimg" -> {
                    listBook.listBgCimg = reader.nextString()
                }
                "list_text_color" -> {
                    listBook.listTextColor = reader.nextString()
                }
                "list_budget" -> {
                    listBook.listBudget = reader.nextDouble()
                }
                "list_budget_show" -> {
                    listBook.listBudgetShow = reader.nextString()
                }
                "list_budget_ishide" -> {
                    listBook.listBudgetIshide = reader.nextInt()
                }
                "list_budget_ratename" -> {
                    listBook.listBudgetRatename = reader.nextString()
                }
                "rate_name" -> {
                    listBook.rateName = reader.nextString()
                }
                "rate_list" -> {
                    listBook.rateList = reader.nextString()
                }
                "list_rate_budget" -> {
                    listBook.listRateBudget = reader.nextDouble()
                }
                "date_start" -> {
                    listBook.dateStart = reader.nextInt()
                }
                "unselectAccountIds" -> {
                    listBook.unselectAccountIds = reader.nextString()
                }
                "bg_img_mtime" -> {
                    listBook.bgImgMtime = reader.nextLong()
                }
            }
        }
        reader.endObject()
        return listBook
    }
}