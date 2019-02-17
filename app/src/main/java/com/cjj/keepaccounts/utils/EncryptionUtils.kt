package com.cjj.keepaccounts.utils

/**
 * Created by CJJ on 2018/7/29 9:23.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
object EncryptionUtils {
    @JvmStatic
    fun encryptStr(str: String, key: Int): String {
        val chars = str.toCharArray()
        val newChar = CharArray(chars.size)
        for (i in chars.indices) {
            val intC = chars[i].toInt()
            val i1 = if (key % 2 == 0) intC + key else intC - key
            newChar[i] = i1.toChar()
        }
        return String(newChar)
    }

    @JvmStatic
    fun decodeStr(str: String, key: Int): String {
        val chars = str.toCharArray()
        val newChar = CharArray(chars.size)
        for (i in chars.indices) {
            val intC = chars[i].toInt()
            val i1 = if (key % 2 == 0) intC - key else intC + key
            newChar[i] = i1.toChar()
        }
        return String(newChar)
    }

    @JvmStatic
    fun encryptMoney(money: Double, key: Int): Double {
        return if (key % 2 == 0) money + key else money - key
    }

    @JvmStatic
    fun decodeMoney(money: Double, key: Int): Double {
        return if (key % 2 == 0) money - key else money + key
    }
}