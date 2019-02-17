package com.cjj.keepaccounts.mvp.activity.account.selectbank

import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.bean.BankInfoBean
import com.cjj.keepaccounts.manager.LogoManager
import javax.inject.Inject

/**
 * Created by CJJ on 2019/1/30 17:32.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class MSelectBank @Inject constructor() : CSelectBank.Model {

    override val backs = arrayListOf<BankInfoBean>()

    private val resultBacks = arrayListOf<BankInfoBean>()

    private val indexMap = hashMapOf<Char, Int>()

    init {
        val colorList = LogoManager.AccountColor.colorList
        backs.add(BankInfoBean("工商银行", R.mipmap.bank1_gongshang, colorList[12], '常', false))
        backs.add(BankInfoBean("建设银行", R.mipmap.bank1_jianshe, colorList[3], '常', false))
        backs.add(BankInfoBean("交通银行", R.mipmap.bank1_jiaotong, colorList[3], '常', false))
        backs.add(BankInfoBean("农业银行", R.mipmap.bank1_nongye, colorList[8], '常', false))
        backs.add(BankInfoBean("中国银行", R.mipmap.bank1_zhongguo, colorList[12], '常', false))
        backs.add(BankInfoBean("招商银行", R.mipmap.bank1_zhaoshang, colorList[12], '常', false))
        backs.add(BankInfoBean("邮政储蓄", R.mipmap.bank1_youzheng, colorList[8], '常', false))
        backs.add(BankInfoBean("民生银行", R.mipmap.bank1_minsheng, colorList[3], '常', false))
        backs.add(BankInfoBean("兴业银行", R.mipmap.bank1_xingye, colorList[3], '常', false))
        backs.add(BankInfoBean("中信银行", R.mipmap.bank1_zhongxin, colorList[12], '常', false))
        backs.add(BankInfoBean("平安银行", R.mipmap.bank_zhongguopingan, colorList[10], '常', false))
        backs.add(BankInfoBean("广发银行", R.mipmap.bank1_guangfa, colorList[12], '常', false))
        backs.add(BankInfoBean("光大银行", R.mipmap.bank1_guangda, colorList[1], '常', false))
        backs.add(BankInfoBean("其他银行", R.mipmap.bank1_qita, colorList[4], '常', false))

        backs.add(BankInfoBean("北京农商银行", R.mipmap.bank1_beijingnongshang, colorList[12], 'B', false))
        backs.add(BankInfoBean("包商银行", R.mipmap.baoshangyinhang1, colorList[10], 'B', false))
        backs.add(BankInfoBean("渤海银行", R.mipmap.bank1_bohai, colorList[3], 'B', false))
        backs.add(BankInfoBean("北京银行", R.mipmap.bank1_beijing, colorList[12], 'B', false))


        backs.add(BankInfoBean("长沙银行", R.mipmap.bank1_changsha, colorList[12], 'C', false))
        backs.add(BankInfoBean("成都农商银行", R.mipmap.bank1_chengdunongshang, colorList[8], 'C', false))
        backs.add(BankInfoBean("成都银行", R.mipmap.bank1_chengdu, colorList[12], 'C', false))
        backs.add(BankInfoBean("重庆银行", R.mipmap.bank1_chongqing, colorList[8], 'C', false))


        backs.add(BankInfoBean("大连银行", R.mipmap.bank1_dalian, colorList[12], 'D', false))
        backs.add(BankInfoBean("东莞银行", R.mipmap.bank1_dongguan, colorList[12], 'D', false))
        backs.add(BankInfoBean("东莞农商银行", R.mipmap.dongguannonghsangyinhang1, colorList[12], 'D', false))
        backs.add(BankInfoBean("东亚银行", R.mipmap.dongyayinhang1, colorList[12], 'D', false))

        backs.add(BankInfoBean("福建农信", R.mipmap.nongcunxinyongshe1, colorList[8], 'F', false))

        backs.add(BankInfoBean("甘肃银行", R.mipmap.bank1_gansu, colorList[11], 'G', false))
        backs.add(BankInfoBean("广州银行", R.mipmap.bank1_guangzhou, colorList[12], 'G', false))
        backs.add(BankInfoBean("广州农商银行", R.mipmap.bank1_guangzhounongshang, colorList[10], 'G', false))
        backs.add(BankInfoBean("工商银行", R.mipmap.bank1_gongshang, colorList[12], 'G', false))
        backs.add(BankInfoBean("广发银行", R.mipmap.bank1_guangfa, colorList[12], 'G', false))
        backs.add(BankInfoBean("光大银行", R.mipmap.bank1_guangda, colorList[1], 'G', false))


        backs.add(BankInfoBean("杭州银行", R.mipmap.bank1_hangzhou, colorList[6], 'H', false))
        backs.add(BankInfoBean("花旗银行", R.mipmap.huaqiyinhang1, colorList[3], 'H', false))
        backs.add(BankInfoBean("哈尔滨银行", R.mipmap.huaerbinyinhang1, colorList[1], 'H', false))
        backs.add(BankInfoBean("汇丰银行", R.mipmap.huifengyinhang1, colorList[10], 'H', false))
        backs.add(BankInfoBean("河北银行", R.mipmap.bank1_heibei, colorList[14], 'H', false))
        backs.add(BankInfoBean("恒丰银行", R.mipmap.hengfengyinhang1, colorList[3], 'H', false))
        backs.add(BankInfoBean("华夏银行", R.mipmap.bank1_huaxia, colorList[12], 'H', false))

        backs.add(BankInfoBean("吉林银行", R.mipmap.bank1_jilin, colorList[12], 'J', false))
        backs.add(BankInfoBean("江苏银行", R.mipmap.bank1_jiangsu, colorList[4], 'J', false))
        backs.add(BankInfoBean("建设银行", R.mipmap.bank1_jianshe, colorList[3], 'J', false))
        backs.add(BankInfoBean("交通银行", R.mipmap.bank1_jiaotong, colorList[3], 'J', false))
        backs.add(BankInfoBean("江南农商银行", R.mipmap.jiangnannongcunshangyeyinhang1, colorList[16], 'J', false))


        backs.add(BankInfoBean("昆仑银行", R.mipmap.hunlunyinhang1, colorList[12], 'K', false))

        backs.add(BankInfoBean("兰州银行", R.mipmap.bank1_lanzhou, colorList[3], 'L', false))

        backs.add(BankInfoBean("民泰银行", R.mipmap.bank1_mintai, colorList[12], 'M', false))
        backs.add(BankInfoBean("民生银行", R.mipmap.bank1_minsheng, colorList[3], 'M', false))

        backs.add(BankInfoBean("南京银行", R.mipmap.bank1_nanjing, colorList[12], 'N', false))
        backs.add(BankInfoBean("农村信用社", R.mipmap.nongcunxinyongshe1, colorList[8], 'N', false))
        backs.add(BankInfoBean("内蒙古银行", R.mipmap.bank1_neimenggu, colorList[12], 'N', false))
        backs.add(BankInfoBean("宁波银行", R.mipmap.bank1_ningbo, colorList[10], 'N', false))
        backs.add(BankInfoBean("宁夏银行", R.mipmap.bank1_ningxia, colorList[12], 'N', false))
        backs.add(BankInfoBean("农业银行", R.mipmap.bank1_nongye, colorList[8], 'N', false))


        backs.add(BankInfoBean("平安银行", R.mipmap.bank1_zhongguopingan, colorList[10], 'P', false))
        backs.add(BankInfoBean("浦东发展银行", R.mipmap.bank1_pufa, colorList[3], 'P', false))


        backs.add(BankInfoBean("齐鲁银行", R.mipmap.bank1_qilu, colorList[6], 'Q', false))
        backs.add(BankInfoBean("青岛银行", R.mipmap.bank1_qingdao, colorList[12], 'Q', false))
        backs.add(BankInfoBean("青海银行", R.mipmap.bank1_qinghai, colorList[1], 'Q', false))
        backs.add(BankInfoBean("其他银行", R.mipmap.bank1_qita, colorList[4], 'Q', false))


        backs.add(BankInfoBean("上海农商银行", R.mipmap.bank1_shanghainongshang, colorList[3], 'S', false))
        backs.add(BankInfoBean("上海银行", R.mipmap.bank1_shanghai, colorList[3], 'S', false))
        backs.add(BankInfoBean("深圳农商银行", R.mipmap.shenzhennongshangyinhang1, colorList[6], 'S', false))
        backs.add(BankInfoBean("山东农商银行", R.mipmap.shandongnonghsangyinhang1, colorList[12], 'S', false))
        backs.add(BankInfoBean("盛京银行", R.mipmap.shengjingyinhang1, colorList[12], 'S', false))


        backs.add(BankInfoBean("天津银行", R.mipmap.bank1_tianjin, colorList[11], 'T', false))
        backs.add(BankInfoBean("天津农商银行", R.mipmap.tianjinnongshangyinhang1, colorList[3], 'T', false))


        backs.add(BankInfoBean("温州银行", R.mipmap.bank1_wenzhou, colorList[10], 'W', false))


        backs.add(BankInfoBean("兴业银行", R.mipmap.bank1_xingye, colorList[3], 'X', false))


        backs.add(BankInfoBean("邮政储蓄", R.mipmap.bank1_youzheng, colorList[8], 'Y', false))

        backs.add(BankInfoBean("浙商银行", R.mipmap.bank1_zheshang, colorList[10], 'Z', false))
        backs.add(BankInfoBean("中国银行", R.mipmap.bank1_zhongguo, colorList[12], 'Z', false))
        backs.add(BankInfoBean("招商银行", R.mipmap.bank1_zhaoshang, colorList[12], 'Z', false))
        backs.add(BankInfoBean("中信银行", R.mipmap.bank1_zhongxin, colorList[12], 'Z', false))
        backs.add(BankInfoBean("浙江农信", R.mipmap.zhejiangnongxin1, colorList[8], 'Z', false))
        backs.add(BankInfoBean("渣打银行", R.mipmap.zhadayinhang1, colorList[8], 'Z', false))

    }

    override fun searchBank(key: String): ArrayList<BankInfoBean> {
        val tempBacks = ArrayList<BankInfoBean>()
        resultBacks.clear()
        indexMap.clear()
        if (key.isEmpty()) {
            tempBacks.addAll(backs)
        } else {
            (14 until backs.size)
                    .asSequence()
                    .map { backs[it] }
                    .filterTo(tempBacks) { it.backName.contains(key) }
        }

        if (tempBacks.isEmpty()) {

        } else {
            val size = tempBacks.size
            var nodeName = ' '
            for (i in 0 until size) {
                val bankInfoBean = tempBacks[i]
                if (nodeName != bankInfoBean.nodeName) {
                    nodeName = bankInfoBean.nodeName
                    indexMap[nodeName] = resultBacks.size
                    resultBacks.add(BankInfoBean("", R.mipmap.jiansheyinhang, 0, nodeName, true))
                }
                resultBacks.add(bankInfoBean)
            }
            tempBacks.clear()

        }
        return resultBacks
    }

}