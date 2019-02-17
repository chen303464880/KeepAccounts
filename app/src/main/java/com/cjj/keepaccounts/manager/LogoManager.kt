package com.cjj.keepaccounts.manager

import android.graphics.Color
import android.util.SparseArray
import android.util.SparseIntArray
import androidx.core.util.set
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.bean.LogoInfo

/**
 * @author chenjunjie
 * Created by CJJ on 2017/11/29 9:46.
 */
object LogoManager {

    object HomeLogo {
        val homeLogo = SparseIntArray()

        init {
            homeLogo.put(1, R.mipmap.home_xianjin)
            homeLogo.put(2, R.mipmap.home_chuxu)
            homeLogo.put(3, R.mipmap.home_xinyongka)
            homeLogo.put(4, R.mipmap.home_wangluo)
            homeLogo.put(5, R.mipmap.home_touzizhanghu)
            homeLogo.put(6, R.mipmap.home_chuzhi)
            homeLogo.put(7, R.mipmap.home_yingshou)
            homeLogo.put(8, R.mipmap.home_yingfu)
            homeLogo.put(9, R.mipmap.home_zhifubao)
            homeLogo.put(10, R.mipmap.home_weixin)
            homeLogo.put(11, R.mipmap.home_gushou)
            homeLogo.put(12, R.mipmap.home_mayihuabei)
            homeLogo.put(13, R.mipmap.home_jingdongbaitiao)
        }
    }

    object TypeLogo {
        val typeLogo = SparseIntArray()

        init {
            typeLogo.put(-110, R.mipmap.lixizhichu)
            typeLogo.put(-109, R.mipmap.huankuan)
            typeLogo.put(-108, R.mipmap.huankuan)
            typeLogo.put(-107, R.mipmap.icon_shouru_type_jieru)
            typeLogo.put(-106, R.mipmap.icon_shouru_type_jieru)
            typeLogo.put(-105, R.mipmap.lixishouru)
            typeLogo.put(-104, R.mipmap.icon_shouru_type_jieru)
            typeLogo.put(-103, R.mipmap.icon_shouru_type_jieru)
            typeLogo.put(-102, R.mipmap.icon_zhichu_type_jiechu)
            typeLogo.put(-101, R.mipmap.icon_zhichu_type_jiechu)
            typeLogo.put(-11, R.mipmap.icon_shouru_type_hongbao)
            typeLogo.put(-10, R.mipmap.touzishouyi)
            typeLogo.put(-9, R.mipmap.touzilicai)
            typeLogo.put(-8, R.mipmap.icon_shouru_type_touzishouru)
            typeLogo.put(-7, R.mipmap.icon_add_9)
            typeLogo.put(-6, R.mipmap.touzilicai)
            typeLogo.put(-5, R.mipmap.touzilicai)
            typeLogo.put(-4, R.mipmap.icon_zhichu_type_jiechu)
            typeLogo.put(-3, R.mipmap.icon_shouru_type_jieru)
            typeLogo.put(-2, R.mipmap.yuebiangeng)
            typeLogo.put(-1, R.mipmap.zhuanzhang1)
            typeLogo.put(1, R.mipmap.icon_zhichu_type_yiban)
            typeLogo.put(2, R.mipmap.icon_zhichu_type_shuji)
            typeLogo.put(3, R.mipmap.icon_jiaotongchuxing)
            typeLogo.put(4, R.mipmap.icon_zhichu_type_renqingsongli)
            typeLogo.put(5, R.mipmap.icon_zhichu_type_jiechu)
            typeLogo.put(6, R.mipmap.icon_zhichu_type_yiliaojiaoyu)
            typeLogo.put(7, R.mipmap.icon_zhichu_type_yule)
            typeLogo.put(8, R.mipmap.icon_zhichu_type_chongwu)
            typeLogo.put(9, R.mipmap.icon_zhichu_type_jujia)
            typeLogo.put(10, R.mipmap.icon_zhichu_type_shoujitongxun)
            typeLogo.put(12, R.mipmap.icon_zhichu_type_shuiguolingshi)
            typeLogo.put(13, R.mipmap.icon_zhichu_type_taobao)
            typeLogo.put(14, R.mipmap.icon_zhichu_type_meirongjianshen)
            typeLogo.put(15, R.mipmap.icon_zhichu_type_gouwu)
            typeLogo.put(16, R.mipmap.jiushui)
            typeLogo.put(17, R.mipmap.icon_zhichu_type_canyin)
            typeLogo.put(18, R.mipmap.icon_shouru_type_qita)
            typeLogo.put(19, R.mipmap.icon_shouru_type_jianzhiwaikuai)
            typeLogo.put(20, R.mipmap.icon_shouru_type_jiangjin)
            typeLogo.put(21, R.mipmap.icon_shouru_type_gongzi)
            typeLogo.put(22, R.mipmap.icon_shouru_type_touzishouru)
            typeLogo.put(23, R.mipmap.icon_shouru_type_shenghuofei)
            typeLogo.put(24, R.mipmap.icon_shouru_type_hongbao)
            typeLogo.put(25, R.mipmap.icon_shouru_type_linghuaqian)
            typeLogo.put(201, R.mipmap.zhusu)
            typeLogo.put(202, R.mipmap.icon_jiaotongchuxing)
            typeLogo.put(203, R.mipmap.icon_zhichu_type_canyin)
            typeLogo.put(204, R.mipmap.icon_lingshixiaochi)
            typeLogo.put(205, R.mipmap.icon_menpiao)
            typeLogo.put(206, R.mipmap.icon_gouwu)
            typeLogo.put(207, R.mipmap.icon_zhichu_type_renqingsongli)
            typeLogo.put(208, R.mipmap.icon_yule)
            typeLogo.put(209, R.mipmap.icon_lvxingtuanfei)
            typeLogo.put(210, R.mipmap.icon_lvxingzhuangbei)
            typeLogo.put(211, R.mipmap.icon_zhichu_type_yiban)
            typeLogo.put(212, R.mipmap.icon_youqingzanzhu)
            typeLogo.put(213, R.mipmap.baoxiao)
            typeLogo.put(214, R.mipmap.icon_shouru_type_hongbao)
            typeLogo.put(215, R.mipmap.icon_shouru_type_jiangjin)
            typeLogo.put(216, R.mipmap.icon_shouru_type_jianzhiwaikuai)
            typeLogo.put(217, R.mipmap.icon_shouru_type_qita)
            typeLogo.put(301, R.mipmap.icon_add_16)
            typeLogo.put(302, R.mipmap.icon_menmianzujin)
            typeLogo.put(303, R.mipmap.icon_jaotongyunshu)
            typeLogo.put(304, R.mipmap.icon_bangongyongpin)
            typeLogo.put(305, R.mipmap.icon_add_5)
            typeLogo.put(306, R.mipmap.icon_shichanghuodong)
            typeLogo.put(307, R.mipmap.icon_peishangfakuan)
            typeLogo.put(308, R.mipmap.anjie)
            typeLogo.put(309, R.mipmap.icon_yingyeshouru)
            typeLogo.put(310, R.mipmap.icon_lingshou)
            typeLogo.put(311, R.mipmap.icon_daikuan)
            typeLogo.put(312, R.mipmap.icon_daigou)
            typeLogo.put(313, R.mipmap.icon_weishang)
            typeLogo.put(314, R.mipmap.icon_zhichu_type_taobao)
            typeLogo.put(315, R.mipmap.icon_shouru_type_qita)
            typeLogo.put(1001, R.mipmap.icon_add_1)
            typeLogo.put(1002, R.mipmap.icon_add_2)
            typeLogo.put(1003, R.mipmap.icon_add_3)
            typeLogo.put(1005, R.mipmap.icon_add_5)
            typeLogo.put(1007, R.mipmap.icon_add_7)
            typeLogo.put(1008, R.mipmap.icon_add_8)
            typeLogo.put(1010, R.mipmap.icon_add_10)
            typeLogo.put(1011, R.mipmap.icon_add_11)
            typeLogo.put(1012, R.mipmap.icon_add_12)
            typeLogo.put(1014, R.mipmap.icon_add_14)
            typeLogo.put(1016, R.mipmap.icon_add_16)
            typeLogo.put(1019, R.mipmap.icon_add_19)
            typeLogo.put(1020, R.mipmap.icon_add_20)
            typeLogo.put(3001, R.mipmap.lingshi)
            typeLogo.put(3002, R.mipmap.zaocan)
            typeLogo.put(3003, R.mipmap.zhongfan)
            typeLogo.put(3004, R.mipmap.wanfan)
            typeLogo.put(3005, R.mipmap.chashuikafei)
            typeLogo.put(3006, R.mipmap.fanka)
            typeLogo.put(3008, R.mipmap.xiaochi)
            typeLogo.put(3009, R.mipmap.yifu)
            typeLogo.put(3010, R.mipmap.kuzi)
            typeLogo.put(3011, R.mipmap.xiezi)
            typeLogo.put(3012, R.mipmap.mao)
            typeLogo.put(3013, R.mipmap.baobao)
            typeLogo.put(3014, R.mipmap.huazhuangpin)
            typeLogo.put(3015, R.mipmap.shipin)
            typeLogo.put(3016, R.mipmap.dianfei)
            typeLogo.put(3017, R.mipmap.shuifei)
            typeLogo.put(3018, R.mipmap.sijiache)
            typeLogo.put(3019, R.mipmap.gonggongqiche)
            typeLogo.put(3020, R.mipmap.fangdai)
            typeLogo.put(3021, R.mipmap.wuye)
            typeLogo.put(3022, R.mipmap.tingchefei)
            typeLogo.put(3023, R.mipmap.weixiubaoyang)
            typeLogo.put(3024, R.mipmap.huochepiao)
            typeLogo.put(3025, R.mipmap.feijipiao)
            typeLogo.put(3026, R.mipmap.chuanpiao)
            typeLogo.put(3027, R.mipmap.zuojifei)
            typeLogo.put(3028, R.mipmap.wangfei)
            typeLogo.put(3029, R.mipmap.richangyongpin)
            typeLogo.put(3030, R.mipmap.wanggou)
            typeLogo.put(3031, R.mipmap.haiwaidaigou)
            typeLogo.put(3032, R.mipmap.shumachanpin)
            typeLogo.put(3033, R.mipmap.naifen)
            typeLogo.put(3034, R.mipmap.xuefei)
            typeLogo.put(3035, R.mipmap.baojian)
            typeLogo.put(3036, R.mipmap.yaopinfei)
            typeLogo.put(3037, R.mipmap.lifa)
            typeLogo.put(3038, R.mipmap.xizao)
            typeLogo.put(3039, R.mipmap.ad)
            typeLogo.put(3040, R.mipmap.dianying)
            typeLogo.put(3041, R.mipmap.lvyoudujia)
            typeLogo.put(3042, R.mipmap.youxi)
            typeLogo.put(3043, R.mipmap.yundongjianshen)
            typeLogo.put(3044, R.mipmap.wanju)
            typeLogo.put(3045, R.mipmap.party)
            typeLogo.put(3046, R.mipmap.dapai)
            typeLogo.put(3047, R.mipmap.majiang)
            typeLogo.put(3048, R.mipmap.fuwu)
            typeLogo.put(3049, R.mipmap.huwaishebei)
            typeLogo.put(3050, R.mipmap.yiwaisuode)
            typeLogo.put(3051, R.mipmap.daoyou)
            typeLogo.put(3052, R.mipmap.xiaojingjiazhang)
            typeLogo.put(3053, R.mipmap.tuikuan)
            typeLogo.put(3054, R.mipmap.huankuan)
            typeLogo.put(3055, R.mipmap.xianjin)
            typeLogo.put(3056, R.mipmap.baoxiao)
            typeLogo.put(3057, R.mipmap.xinyongkahuankuan)
            typeLogo.put(3058, R.mipmap.lingqian)
            typeLogo.put(3059, R.mipmap.quxian)
            typeLogo.put(3060, R.mipmap.baoxian)
            typeLogo.put(3061, R.mipmap.yinhangshouxufei)
            typeLogo.put(3062, R.mipmap.anjie)
            typeLogo.put(3063, R.mipmap.icon_shouru_type_qita)
            typeLogo.put(3064, R.mipmap.youfei)
            typeLogo.put(3065, R.mipmap.zawu)
            typeLogo.put(3066, R.mipmap.yiwaiposun)
            typeLogo.put(3067, R.mipmap.maicai)
            typeLogo.put(3068, R.mipmap.fangzu)
            typeLogo.put(3069, R.mipmap.yan)
            typeLogo.put(3070, R.mipmap.zhifubao)
        }
    }


    object Logo {
        val logo = ArrayList<LogoInfo>()
        val logoSparse: SparseArray<LogoInfo> by lazy(LazyThreadSafetyMode.NONE) {
            val sparseArray = SparseArray<LogoInfo>()
            logo.forEach {
                sparseArray[it.imgIndex] = it
            }
            sparseArray[-109] = LogoInfo(-109, R.mipmap.huankuan, Color.parseColor("#FFFBADC7"))
            sparseArray[-108] = LogoInfo(-108, R.mipmap.huankuan, Color.parseColor("#FFFBADC7"))
            sparseArray[-107] = LogoInfo(-107, R.mipmap.icon_shouru_type_jieru, Color.parseColor("#FFB5A353"))
            sparseArray[-106] = LogoInfo(-106, R.mipmap.icon_shouru_type_jieru, Color.parseColor("#FFB5A353"))
            sparseArray[-104] = LogoInfo(-104, R.mipmap.icon_shouru_type_jieru, Color.parseColor("#FFB5A353"))
            sparseArray[-103] = LogoInfo(-103, R.mipmap.icon_shouru_type_jieru, Color.parseColor("#FFB5A353"))
            sparseArray[-102] = LogoInfo(-102, R.mipmap.icon_zhichu_type_jiechu, Color.parseColor("#FFd84367"))
            sparseArray[-101] = LogoInfo(-101, R.mipmap.icon_zhichu_type_jiechu, Color.parseColor("#FFd84367"))
            sparseArray[-11] = LogoInfo(-11, R.mipmap.icon_shouru_type_hongbao, Color.parseColor("#FFFF9397"))
            sparseArray[-9] = LogoInfo(-9, R.mipmap.touzilicai, Color.parseColor("#FF6666AA"))
            sparseArray[-8] = LogoInfo(-8, R.mipmap.icon_shouru_type_touzishouru, Color.parseColor("#FFCC6603"))
            sparseArray[-6] = LogoInfo(-6, R.mipmap.touzilicai, Color.parseColor("#FF6666AA"))
            sparseArray[-5] = LogoInfo(-5, R.mipmap.touzilicai, Color.parseColor("#FF6666AA"))
            sparseArray[-4] = LogoInfo(-4, R.mipmap.icon_zhichu_type_jiechu, Color.parseColor("#FFd84367"))
            sparseArray[1] = LogoInfo(1, R.mipmap.icon_zhichu_type_yiban, Color.parseColor("#FF3EA6D6"))
            sparseArray[3] = LogoInfo(3, R.mipmap.icon_jiaotongchuxing, Color.parseColor("#FFA2B6FF"))
            sparseArray[203] = LogoInfo(203, R.mipmap.icon_zhichu_type_canyin, Color.parseColor("#FFFEC87B"))
            sparseArray[207] = LogoInfo(207, R.mipmap.icon_zhichu_type_renqingsongli, Color.parseColor("#FFDBA7BC"))
            sparseArray[211] = LogoInfo(211, R.mipmap.icon_zhichu_type_yiban, Color.parseColor("#FF3EA6D6"))
            sparseArray[213] = LogoInfo(213, R.mipmap.baoxiao, Color.parseColor("#FFB0D5CA"))
            sparseArray[214] = LogoInfo(214, R.mipmap.icon_shouru_type_hongbao, Color.parseColor("#FFFF9397"))
            sparseArray[215] = LogoInfo(215, R.mipmap.icon_shouru_type_jiangjin, Color.parseColor("#FFED9241"))
            sparseArray[216] = LogoInfo(216, R.mipmap.icon_shouru_type_jianzhiwaikuai, Color.parseColor("#FF5FB0C5"))
            sparseArray[217] = LogoInfo(217, R.mipmap.icon_shouru_type_qita, Color.parseColor("#FF9ADCF7"))
            sparseArray[301] = LogoInfo(301, R.mipmap.icon_add_16, Color.parseColor("#FF61D2D6"))
            sparseArray[305] = LogoInfo(305, R.mipmap.icon_add_5, Color.parseColor("#FF6797A8"))
            sparseArray[308] = LogoInfo(308, R.mipmap.anjie, Color.parseColor("#FFA2CA9F"))
            sparseArray[314] = LogoInfo(314, R.mipmap.icon_zhichu_type_taobao, Color.parseColor("#FFFAC66C"))
            sparseArray
        }

        init {
            logo.add(LogoInfo(17, R.mipmap.icon_zhichu_type_canyin, Color.parseColor("#FFFEC87B")))
            logo.add(LogoInfo(3067, R.mipmap.maicai, Color.parseColor("#FF99DED3")))
            logo.add(LogoInfo(16, R.mipmap.jiushui, Color.parseColor("#FF9ECAFB")))
            logo.add(LogoInfo(12, R.mipmap.icon_zhichu_type_shuiguolingshi, Color.parseColor("#FFF5DA6D")))
            logo.add(LogoInfo(3069, R.mipmap.yan, Color.parseColor("#FFA5B4D8")))

            logo.add(LogoInfo(3001, R.mipmap.lingshi, Color.parseColor("#FFBAE081")))
            logo.add(LogoInfo(3002, R.mipmap.zaocan, Color.parseColor("#FFA3D5A9")))
            logo.add(LogoInfo(3003, R.mipmap.zhongfan, Color.parseColor("#FF92DCC9")))
            logo.add(LogoInfo(3004, R.mipmap.wanfan, Color.parseColor("#FFF2CF94")))
            logo.add(LogoInfo(3005, R.mipmap.chashuikafei, Color.parseColor("#FFEBB5A4")))


            logo.add(LogoInfo(3006, R.mipmap.fanka, Color.parseColor("#FFE7C6B1")))
            logo.add(LogoInfo(204, R.mipmap.icon_lingshixiaochi, Color.parseColor("#FFFFB588")))
            logo.add(LogoInfo(1007, R.mipmap.icon_add_7, Color.parseColor("#FFC0AB58")))
            logo.add(LogoInfo(3008, R.mipmap.xiaochi, Color.parseColor("#FFDAC9F7")))
            logo.add(LogoInfo(1008, R.mipmap.icon_add_8, Color.parseColor("#FFEEBB96")))

            logo.add(LogoInfo(3009, R.mipmap.yifu, Color.parseColor("#FFC7ADF5")))
            logo.add(LogoInfo(3010, R.mipmap.kuzi, Color.parseColor("#FFEBD4A6")))
            logo.add(LogoInfo(3011, R.mipmap.xiezi, Color.parseColor("#FFFDC294")))
            logo.add(LogoInfo(3012, R.mipmap.mao, Color.parseColor("#FF99E5C7")))
            logo.add(LogoInfo(3013, R.mipmap.baobao, Color.parseColor("#FF8FB2EC")))


            logo.add(LogoInfo(3014, R.mipmap.huazhuangpin, Color.parseColor("#FFF0A8D4")))
            logo.add(LogoInfo(3015, R.mipmap.shipin, Color.parseColor("#FFEEA9C0")))
            logo.add(LogoInfo(14, R.mipmap.icon_zhichu_type_meirongjianshen, Color.parseColor("#FFE2728E")))
            logo.add(LogoInfo(3068, R.mipmap.fangzu, Color.parseColor("#FF90BADF")))
            logo.add(LogoInfo(201, R.mipmap.zhusu, Color.parseColor("#FFACC9D7")))

            logo.add(LogoInfo(9, R.mipmap.icon_zhichu_type_jujia, Color.parseColor("#FFB36A66")))
            logo.add(LogoInfo(3016, R.mipmap.dianfei, Color.parseColor("#FF8ED4A6")))
            logo.add(LogoInfo(3017, R.mipmap.shuifei, Color.parseColor("#FFA7D4DB")))
            logo.add(LogoInfo(202, R.mipmap.icon_jiaotongchuxing, Color.parseColor("#FFA2B6FF")))
            logo.add(LogoInfo(1019, R.mipmap.icon_add_19, Color.parseColor("#FF80C7C3")))


            logo.add(LogoInfo(3018, R.mipmap.sijiache, Color.parseColor("#FF95BDFF")))
            logo.add(LogoInfo(3019, R.mipmap.gonggongqiche, Color.parseColor("#FF99DBAE")))
            logo.add(LogoInfo(3020, R.mipmap.fangdai, Color.parseColor("#FFC6BBD8")))
            logo.add(LogoInfo(3021, R.mipmap.wuye, Color.parseColor("#FFCCE3AF")))
            logo.add(LogoInfo(3022, R.mipmap.tingchefei, Color.parseColor("#FFF7A088")))

            logo.add(LogoInfo(3023, R.mipmap.weixiubaoyang, Color.parseColor("#FFABDAF0")))
            logo.add(LogoInfo(1002, R.mipmap.icon_add_2, Color.parseColor("#FF66A6D1")))
            logo.add(LogoInfo(3024, R.mipmap.huochepiao, Color.parseColor("#FF9DC7E4")))
            logo.add(LogoInfo(303, R.mipmap.icon_jaotongyunshu, Color.parseColor("#FFD18A37")))
            logo.add(LogoInfo(3025, R.mipmap.feijipiao, Color.parseColor("#FFBAACD4")))


            logo.add(LogoInfo(3026, R.mipmap.chuanpiao, Color.parseColor("#FF84EFE9")))
            logo.add(LogoInfo(13, R.mipmap.icon_zhichu_type_taobao, Color.parseColor("#FFFAC66C")))
            logo.add(LogoInfo(10, R.mipmap.icon_zhichu_type_shoujitongxun, Color.parseColor("#FF85C5EE")))
            logo.add(LogoInfo(3027, R.mipmap.zuojifei, Color.parseColor("#FF79AAD6")))
            logo.add(LogoInfo(3028, R.mipmap.wangfei, Color.parseColor("#FFBEA8D7")))

            logo.add(LogoInfo(3029, R.mipmap.richangyongpin, Color.parseColor("#FFC0BAEB")))
            logo.add(LogoInfo(6, R.mipmap.icon_zhichu_type_yiliaojiaoyu, Color.parseColor("#FFEB8ABE")))
            logo.add(LogoInfo(206, R.mipmap.icon_gouwu, Color.parseColor("#FFA5DCAA")))
            logo.add(LogoInfo(15, R.mipmap.icon_zhichu_type_gouwu, Color.parseColor("#FFE7C03E")))
            logo.add(LogoInfo(3030, R.mipmap.wanggou, Color.parseColor("#FFF7B6FF")))


            logo.add(LogoInfo(3031, R.mipmap.haiwaidaigou, Color.parseColor("#FFF49F8B")))
            logo.add(LogoInfo(8, R.mipmap.icon_zhichu_type_chongwu, Color.parseColor("#FFA1B2A5")))
            logo.add(LogoInfo(3032, R.mipmap.shumachanpin, Color.parseColor("#FFB8C5DE")))
            logo.add(LogoInfo(3033, R.mipmap.naifen, Color.parseColor("#FFF5A6A9")))
            logo.add(LogoInfo(1014, R.mipmap.icon_add_14, Color.parseColor("#FFE880A2")))

            logo.add(LogoInfo(1020, R.mipmap.icon_add_20, Color.parseColor("#FFBFB2BE")))
            logo.add(LogoInfo(302, R.mipmap.icon_menmianzujin, Color.parseColor("#FF6D6E89")))
            logo.add(LogoInfo(3034, R.mipmap.xuefei, Color.parseColor("#FFD6DA6A")))
            logo.add(LogoInfo(2, R.mipmap.icon_zhichu_type_shuji, Color.parseColor("#FF9E7866")))
            logo.add(LogoInfo(3035, R.mipmap.baojian, Color.parseColor("#FF95E7F3")))


            logo.add(LogoInfo(312, R.mipmap.icon_daigou, Color.parseColor("#FFB3B671")))
            logo.add(LogoInfo(1016, R.mipmap.icon_add_16, Color.parseColor("#FF61D2D6")))
            logo.add(LogoInfo(304, R.mipmap.icon_bangongyongpin, Color.parseColor("#FFF963A0")))
            logo.add(LogoInfo(3036, R.mipmap.yaopinfei, Color.parseColor("#FFE0CCB4")))
            logo.add(LogoInfo(3037, R.mipmap.lifa, Color.parseColor("#FFFFB8CF")))

            logo.add(LogoInfo(3038, R.mipmap.xizao, Color.parseColor("#FF8BCFFF")))
            logo.add(LogoInfo(3039, R.mipmap.ad, Color.parseColor("#FF9F9CC1")))
            logo.add(LogoInfo(3040, R.mipmap.dianying, Color.parseColor("#FF9BDEE2")))
            logo.add(LogoInfo(3041, R.mipmap.lvyoudujia, Color.parseColor("#FF80EAD8")))
            logo.add(LogoInfo(7, R.mipmap.icon_zhichu_type_yule, Color.parseColor("#FFD6A834")))


            logo.add(LogoInfo(3042, R.mipmap.youxi, Color.parseColor("#FFBEBFEB")))
            logo.add(LogoInfo(205, R.mipmap.icon_menpiao, Color.parseColor("#FFB55A42")))
            logo.add(LogoInfo(208, R.mipmap.icon_yule, Color.parseColor("#FFD8BFEB")))
            logo.add(LogoInfo(306, R.mipmap.icon_shichanghuodong, Color.parseColor("#FFE56274")))
            logo.add(LogoInfo(1005, R.mipmap.icon_add_5, Color.parseColor("#FF6797A8")))

            logo.add(LogoInfo(3043, R.mipmap.yundongjianshen, Color.parseColor("#FFC3D8DE")))
            logo.add(LogoInfo(210, R.mipmap.icon_lvxingzhuangbei, Color.parseColor("#FF1A94CA")))
            logo.add(LogoInfo(3044, R.mipmap.wanju, Color.parseColor("#FF9BD5ED")))
            logo.add(LogoInfo(3045, R.mipmap.party, Color.parseColor("#FFFF90B2")))
            logo.add(LogoInfo(3046, R.mipmap.dapai, Color.parseColor("#FF8DA0CE")))


            logo.add(LogoInfo(3047, R.mipmap.majiang, Color.parseColor("#FF71CD9B")))
            logo.add(LogoInfo(3048, R.mipmap.fuwu, Color.parseColor("#FFB6CA9E")))
            logo.add(LogoInfo(3049, R.mipmap.huwaishebei, Color.parseColor("#FFC8CCCE")))
            logo.add(LogoInfo(3050, R.mipmap.yiwaisuode, Color.parseColor("#FFF0E094")))
            logo.add(LogoInfo(3051, R.mipmap.daoyou, Color.parseColor("#FFC6E0DA")))

            logo.add(LogoInfo(24, R.mipmap.icon_shouru_type_hongbao, Color.parseColor("#FFFF9397")))
            logo.add(LogoInfo(4, R.mipmap.icon_zhichu_type_renqingsongli, Color.parseColor("#FFDBA7BC")))
            logo.add(LogoInfo(5, R.mipmap.icon_zhichu_type_jiechu, Color.parseColor("#FFd84367")))
            logo.add(LogoInfo(-3, R.mipmap.icon_shouru_type_jieru, Color.parseColor("#FFB5A353")))
            logo.add(LogoInfo(3052, R.mipmap.xiaojingjiazhang, Color.parseColor("#FF9390DE")))


            logo.add(LogoInfo(22, R.mipmap.icon_shouru_type_touzishouru, Color.parseColor("#FFCC6603")))
            logo.add(LogoInfo(-1, R.mipmap.zhuanzhang1, Color.parseColor("#FF98A4FE")))
            logo.add(LogoInfo(3053, R.mipmap.tuikuan, Color.parseColor("#FF9ADCB8")))
            logo.add(LogoInfo(3070, R.mipmap.zhifubao, Color.parseColor("#FF71C5FF")))
            logo.add(LogoInfo(21, R.mipmap.icon_shouru_type_gongzi, Color.parseColor("#FF6B83B7")))

            logo.add(LogoInfo(3054, R.mipmap.huankuan, Color.parseColor("#FFFBADC7")))
            logo.add(LogoInfo(3055, R.mipmap.xianjin, Color.parseColor("#FFBE7676")))
            logo.add(LogoInfo(3056, R.mipmap.baoxiao, Color.parseColor("#FFB0D5CA")))
            logo.add(LogoInfo(20, R.mipmap.icon_shouru_type_jiangjin, Color.parseColor("#FFED9241")))
            logo.add(LogoInfo(3057, R.mipmap.xinyongkahuankuan, Color.parseColor("#FFA4D8D7")))


            logo.add(LogoInfo(212, R.mipmap.icon_youqingzanzhu, Color.parseColor("#FF3498DB")))
            logo.add(LogoInfo(3058, R.mipmap.lingqian, Color.parseColor("#FF3B5998")))
            logo.add(LogoInfo(25, R.mipmap.icon_shouru_type_linghuaqian, Color.parseColor("#FF889174")))
            logo.add(LogoInfo(23, R.mipmap.icon_shouru_type_shenghuofei, Color.parseColor("#FFD1B95D")))
            logo.add(LogoInfo(3059, R.mipmap.quxian, Color.parseColor("#FF80E49E")))

            logo.add(LogoInfo(3060, R.mipmap.baoxian, Color.parseColor("#FFE1A4DF")))
            logo.add(LogoInfo(-2, R.mipmap.yuebiangeng, Color.parseColor("#FFFFC632")))
            logo.add(LogoInfo(3061, R.mipmap.yinhangshouxufei, Color.parseColor("#FFEFBD91")))
            logo.add(LogoInfo(3062, R.mipmap.anjie, Color.parseColor("#FFA2CA9F")))
            logo.add(LogoInfo(1001, R.mipmap.icon_add_1, Color.parseColor("#FF8EBCDF")))


            logo.add(LogoInfo(1010, R.mipmap.icon_add_10, Color.parseColor("#FFFF9999")))
            logo.add(LogoInfo(3063, R.mipmap.icon_shouru_type_qita, Color.parseColor("#FF9ADCF7")))
            logo.add(LogoInfo(1011, R.mipmap.icon_add_11, Color.parseColor("#FFFF6B6B")))
            logo.add(LogoInfo(1012, R.mipmap.icon_add_12, Color.parseColor("#FFDF3C3C")))
            logo.add(LogoInfo(313, R.mipmap.icon_weishang, Color.parseColor("#FFD24E4E")))

            logo.add(LogoInfo(310, R.mipmap.icon_lingshou, Color.parseColor("#FF68C6DE")))
            logo.add(LogoInfo(311, R.mipmap.icon_daikuan, Color.parseColor("#FFD6A970")))
            logo.add(LogoInfo(209, R.mipmap.icon_lvxingtuanfei, Color.parseColor("#FF18B273")))
            logo.add(LogoInfo(3064, R.mipmap.youfei, Color.parseColor("#FFB2C9B4")))
            logo.add(LogoInfo(307, R.mipmap.icon_peishangfakuan, Color.parseColor("#FFF5534B")))


            logo.add(LogoInfo(19, R.mipmap.icon_shouru_type_jianzhiwaikuai, Color.parseColor("#FF5FB0C5")))
            logo.add(LogoInfo(3065, R.mipmap.zawu, Color.parseColor("#FFB4BAD2")))
            logo.add(LogoInfo(3066, R.mipmap.yiwaiposun, Color.parseColor("#FFF6C8C9")))
            logo.add(LogoInfo(1003, R.mipmap.icon_add_3, Color.parseColor("#FF26B6DF")))
            logo.add(LogoInfo(-7, R.mipmap.icon_add_9, Color.parseColor("#FFFF7F00")))

            logo.add(LogoInfo(-105, R.mipmap.lixishouru, Color.parseColor("#FF736420")))
            logo.add(LogoInfo(-110, R.mipmap.lixizhichu, Color.parseColor("#FFC05F76")))
            logo.add(LogoInfo(-10, R.mipmap.touzishouyi, Color.parseColor("#FFF98F61")))
        }
    }

    object AccountColor {
        val colorList = SparseIntArray()

        init {
            colorList.put(1, Color.parseColor("#FF9361a2"))
            colorList.put(2, Color.parseColor("#FFAB92ED"))
            colorList.put(3, Color.parseColor("#FF5B7ED5"))
            colorList.put(4, Color.parseColor("#FF5D9CEC"))
            colorList.put(5, Color.parseColor("#FF519DDD"))
            colorList.put(6, Color.parseColor("#FF2FB2E8"))
            colorList.put(7, Color.parseColor("#FF45BDB3"))
            colorList.put(8, Color.parseColor("#FF45BC80"))
            colorList.put(9, Color.parseColor("#FF9FBD45"))
            colorList.put(10, Color.parseColor("#FFF3BD5D"))
            colorList.put(11, Color.parseColor("#FFF3955D"))
            colorList.put(12, Color.parseColor("#FFEA7067"))
            colorList.put(13, Color.parseColor("#FFEC87BF"))
            colorList.put(14, Color.parseColor("#FF52B0C8"))
            colorList.put(15, Color.parseColor("#FF52C5D4"))
            colorList.put(16, Color.parseColor("#FFDDB676"))
            colorList.put(17, Color.parseColor("#FFB9A5C6"))
            colorList.put(18, Color.parseColor("#FFA2BA9D"))
        }

    }

    object MemberColor {
        val memberColor = arrayListOf<Int>()

        init {
            memberColor.add(Color.parseColor("#FF4F77E6"))
            memberColor.add(Color.parseColor("#FFF3BD5F"))
            memberColor.add(Color.parseColor("#FF519DDD"))
            memberColor.add(Color.parseColor("#FFF39C12"))
            memberColor.add(Color.parseColor("#FF5B7ED5"))
            memberColor.add(Color.parseColor("#FFD18A37"))
            memberColor.add(Color.parseColor("#FF6666AA"))
            memberColor.add(Color.parseColor("#FFF3955D"))
            memberColor.add(Color.parseColor("#FF8EBCDF"))
            memberColor.add(Color.parseColor("#FFEA7067"))
            memberColor.add(Color.parseColor("#FF57B0C5"))
            memberColor.add(Color.parseColor("#FFDD3131"))
            memberColor.add(Color.parseColor("#FF45BDD3"))
            memberColor.add(Color.parseColor("#FFFF9999"))
            memberColor.add(Color.parseColor("#FF45BC80"))
            memberColor.add(Color.parseColor("#FFFF9AB6"))
            memberColor.add(Color.parseColor("#FFB5BA3E"))
            memberColor.add(Color.parseColor("#FFEB8ABE"))
            memberColor.add(Color.parseColor("#FFB3B671"))
            memberColor.add(Color.parseColor("#FF9361A2"))
        }
    }


    fun getHomeLogo(idImg: Int): Int {
        return HomeLogo.homeLogo[idImg]
    }

    fun getTypeLogo(idImg: Int): Int {
        return TypeLogo.typeLogo[idImg]
    }

    fun getMemberColor(index: Int): Int {
        return MemberColor.memberColor[index % MemberColor.memberColor.size]
    }

}