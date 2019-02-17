package com.cjj.keepaccounts

import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val json = "[\n" +
                "    {\n" +
                "        \"content\": \"\\u5229\\u606f\",\n" +
                "        \"income\": 0,\n" +
                "        \"img\": \"-110\",\n" +
                "        \"type_id\": \"-110\",\n" +
                "        \"list_id\": \"1\",\n" +
                "        \"theorder\": 0,\n" +
                "        \"device_key\": \"5a34967d-5c56-4c74-bf38-c9f2f543617e\",\n" +
                "        \"user_id\": \"2403705\",\n" +
                "        \"is_deleted\": 0,\n" +
                "        \"mtime\": \"1530013664\",\n" +
                "        \"budget_amount\": \"0.0000\",\n" +
                "        \"budget_type\": 0\n" +
                "    },\n" +
                "    {\n" +
                "        \"content\": \"\\u5229\\u606f\",\n" +
                "        \"income\": 0,\n" +
                "        \"img\": \"-110\",\n" +
                "        \"type_id\": \"-110\",\n" +
                "        \"list_id\": \"1\",\n" +
                "        \"theorder\": 0,\n" +
                "        \"device_key\": \"5a34967d-5c56-4c74-bf38-c9f2f543617e\",\n" +
                "        \"user_id\": \"2403705\",\n" +
                "        \"is_deleted\": 0,\n" +
                "        \"mtime\": \"1530013664\",\n" +
                "        \"budget_amount\": \"0.0000\",\n" +
                "        \"budget_type\": 0\n" +
                "    },\n" +
                "    {\n" +
                "        \"content\": \"\\u5229\\u606f\",\n" +
                "        \"income\": 0,\n" +
                "        \"img\": \"-110\",\n" +
                "        \"type_id\": \"-110\",\n" +
                "        \"list_id\": \"1\",\n" +
                "        \"theorder\": 0,\n" +
                "        \"device_key\": \"5a34967d-5c56-4c74-bf38-c9f2f543617e\",\n" +
                "        \"user_id\": \"2403705\",\n" +
                "        \"is_deleted\": 0,\n" +
                "        \"mtime\": \"1530013664\",\n" +
                "        \"budget_amount\": \"0.0000\",\n" +
                "        \"budget_type\": 0\n" +
                "    },\n" +
                "    {\n" +
                "        \"content\": \"\\u5229\\u606f\",\n" +
                "        \"income\": 0,\n" +
                "        \"img\": \"-110\",\n" +
                "        \"type_id\": \"-110\",\n" +
                "        \"list_id\": \"1\",\n" +
                "        \"theorder\": 0,\n" +
                "        \"device_key\": \"5a34967d-5c56-4c74-bf38-c9f2f543617e\",\n" +
                "        \"user_id\": \"2403705\",\n" +
                "        \"is_deleted\": 0,\n" +
                "        \"mtime\": \"1530013664\",\n" +
                "        \"budget_amount\": \"0.0000\",\n" +
                "        \"budget_type\": 0\n" +
                "    }\n" +
                "]"

//        val gson = GsonBuilder().setPrettyPrinting()
//                .registerTypeAdapter(RecordType::class.javaObjectType, RecordTypeTypeAdapter())
//                .create()
//        val list: MutableList<RecordType> = gson.fromJson<MutableList<RecordType>>(json, object : TypeToken<MutableList<RecordType>>() {}.type)
//
//        val syncs = ArrayList<SyncBase>()
//        for (account in list) {
//            println(account.toString())
//            val syncBase = SyncBase()
//            syncBase.action = if (account.uuid % 2 == 1L) "insert" else "update"
//            syncBase.table = "RecordType"
//            syncBase.data = account
//            syncs.add(syncBase)
//        }
//        println(gson.toJson(syncs))
//

//        println(computingAngle(0F, 0F, 2F, 0F))
//        println(computingAngle(0F, 0F, 2F, 1F))
//        println(computingAngle(0F, 0F, 2F, -1F))
//        println(computingAngle(0F, 0F, -2F, 0F))
//        println(computingAngle(0F, 0F, -2F, 1F))
//        println(computingAngle(0F, 0F, -2F, -1F))
    }

    fun computingAngle(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val a = x2 - x1
        val b = y2 - y1
        return Math.toDegrees(Math.atan2(b.toDouble(), a.toDouble())).toFloat()
    }
}
