package com.cjj.keepaccounts.bean.adapter

import com.cjj.keepaccounts.bean.AppConfig
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * @author CJJ
 * Created by CJJ on 2018/7/20 9:54.
 */
class AppConfigTypeAdapter : TypeAdapter<AppConfig>() {
    override fun write(out: JsonWriter, value: AppConfig) {
        out.beginObject()
        out.name("conf_key").value(value.confKey)
        out.name("conf_val").value(value.confVal)
        out.name("ctime").value(value.cTime)
        out.name("mtime").value(value.mTime)
        out.name("deleted").value(value.isDeleted)

        out.endObject()
    }

    override fun read(reader: JsonReader): AppConfig {
        val appConfig = AppConfig()
        appConfig.userId = ""
        appConfig.confVal2 = ""
        appConfig.confVal3 = ""
        appConfig.deviceId = ""
        reader.beginObject()
        var uuid = System.currentTimeMillis()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "conf_key" -> {
                    appConfig.confKey = reader.nextString()
                    if (appConfig.confKey.startsWith("record_type.is_show")) {
                        appConfig.uuid = appConfig.confKey.substringAfterLast("uuid=").toLong()
                    } else {
                        appConfig.uuid = uuid
                        uuid++
                    }
                }
                "conf_val" -> {
                    appConfig.confVal = reader.nextString()
                }
                "ctime" -> {
                    appConfig.cTime = reader.nextLong() * 1000

                }
                "mtime" -> {
                    appConfig.mTime = reader.nextLong() * 1000

                }
                "is_deleted" -> {
                    appConfig.isDeleted = reader.nextInt()
                }
            }
        }
        reader.endObject()
        return appConfig
    }
}