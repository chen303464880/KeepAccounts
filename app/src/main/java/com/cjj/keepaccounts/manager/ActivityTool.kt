package com.cjj.keepaccounts.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.cjj.keepaccounts.bean.ActivityInfoBean
import java.util.*


/**
 * @author chenjunjie
 * Created by CJJ on 2017/11/29 11:34.
 */
object ActivityTool {
    private val activityStack: Stack<Activity> = Stack()

    /**
     * 添加Activity
     */
    fun addActivity(activity: Activity) {
        activityStack.addElement(activity)
    }

    /**
     * 删除activity
     */
    fun removeActivity(activity: Activity) {
        activityStack.removeElement(activity)
//        if (activityStack.size == 0) {
//            System.exit(0)
//        }
    }

    /**
     * 获取当前的Activity
     */
    fun currentActivity(): Activity {
        return if (activityStack.size > 0) {
            activityStack.lastElement()
        } else {
            throw RuntimeException()
        }
    }

    /**
     * 结束所有的activity
     */
    fun finishAllActivity() {
        activityStack.forEach {
            it.finish()
        }
        activityStack.clear()
        System.exit(0)
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    fun finishActivity(activity: Activity) {
        activityStack.remove(activity)
        activity.finish()
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        val listIterator = activityStack.listIterator()
        while (listIterator.hasNext()) {
            val activity = listIterator.next()
            if (activity.javaClass == cls) {
                listIterator.remove()
                activity.finish()
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun batchFinishActivity(cls: Class<*>) {
        while (activityStack.lastElement().javaClass != cls) {
            activityStack.pop().finish()
        }
    }

    /**
     * 要求最低API为11
     * Activity 跳转
     * 跳转后Finish之前所有的Activity
     *
     * @param context
     * @param goal
     */
    fun skipActivityAndFinishAll(context: Context, goal: Class<*>, bundle: Bundle) {
        val intent = Intent(context, goal)
        intent.putExtras(bundle)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        (context as Activity).finish()
    }

    /**
     * 要求最低API为11
     * Activity 跳转
     * 跳转后Finish之前所有的Activity
     *
     * @param goal
     */
    fun skipActivityAndFinishAll(goal: Class<*>) {
        val activity = currentActivity()
        val intent = Intent(currentActivity(), goal)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
        activity.finish()
    }


    /**
     * Activity 跳转
     */
    inline fun <reified T> skipActivityAndFinish(bundle: Bundle) {
        val activity = currentActivity()
        val intent = Intent(activity, T::class.java)
        intent.putExtras(bundle)
        activity.startActivity(intent)
        activity.finish()
    }

    /**
     * Activity 跳转并关闭当前activity
     */
    inline fun <reified T> skipActivityAndFinish() {
        val activity = currentActivity()
        val intent = Intent(activity, T::class.java)
        activity.startActivity(intent)
        activity.finish()
    }


    /**
     * Activity 跳转
     */
    inline fun <reified T> skipActivity() {
        val currentActivity = currentActivity()
        val intent = Intent(currentActivity, T::class.java)
        currentActivity.startActivity(intent)
    }

    /**
     * Activity 跳转
     */
    inline fun <reified T> skipActivity(activityInfo: ActivityInfoBean? = null) {
        val currentActivity = currentActivity()
        val intent = Intent(currentActivity, T::class.java)
        intent.putExtra("activityInfo", activityInfo)
        currentActivity.startActivity(intent)
    }

    /**
     * Activity 跳转
     */
    inline fun <reified T> skipActivity(info: Parcelable, activityInfo: ActivityInfoBean? = null) {
        val currentActivity = currentActivity()
        val intent = Intent(currentActivity, T::class.java)
        intent.putExtra("info", info)
        intent.putExtra("activityInfo", activityInfo)
        currentActivity.startActivity(intent)
    }

    /**
     * Activity 跳转
     */
    inline fun <reified T> skipActivity(info: Map<String, Parcelable>, activityInfo: ActivityInfoBean? = null) {
        val currentActivity = currentActivity()
        val intent = Intent(currentActivity, T::class.java)
        info.forEach { (k, v) -> intent.putExtra(k, v) }
        intent.putExtra("activityInfo", activityInfo)
        currentActivity.startActivity(intent)
    }

    /**
     * Activity 跳转
     */
    inline fun <reified T> skipActivity(vararg info: Pair<String, Parcelable>, activityInfo: ActivityInfoBean? = null) {
        val currentActivity = currentActivity()
        val intent = Intent(currentActivity, T::class.java)
        info.forEach { (first, second) -> intent.putExtra(first, second) }
        intent.putExtra("activityInfo", activityInfo)
        currentActivity.startActivity(intent)
    }

    /**
     * Activity 跳转
     */
    inline fun <reified T> skipActivity(info: Pair<String, Parcelable>, activityInfo: ActivityInfoBean? = null) {
        val currentActivity = currentActivity()
        val intent = Intent(currentActivity, T::class.java)
        intent.putExtra(info.first, info.second)
        intent.putExtra("activityInfo", activityInfo)
        currentActivity.startActivity(intent)
    }


    inline fun <reified T> skipActivityForResult(requestCode: Int, info: Parcelable, activityInfo: ActivityInfoBean? = null) {
        val currentActivity = currentActivity()
        val intent = Intent(currentActivity, T::class.java)
        intent.putExtra("info", info)
        intent.putExtra("activityInfo", activityInfo)
        currentActivity.startActivityForResult(intent, requestCode)
    }


    inline fun <reified T> skipActivityForResult(bundle: Bundle, requestCode: Int) {
        val activity = currentActivity()
        val intent = Intent(activity, T::class.java)
        intent.putExtras(bundle)
        activity.startActivityForResult(intent, requestCode)
    }


}