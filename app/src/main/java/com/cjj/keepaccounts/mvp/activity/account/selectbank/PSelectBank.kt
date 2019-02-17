package com.cjj.keepaccounts.mvp.activity.account.selectbank

import com.cjj.keepaccounts.activity.account.SelectBankActivity
import javax.inject.Inject

/**
 * Created by CJJ on 2019/1/30 17:32.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class PSelectBank @Inject constructor(view: SelectBankActivity, model: MSelectBank) : CSelectBank.Presenter(view, model) {
    override fun searchBank(key: String) {
        view.setAdapterData(model.searchBank(key))
    }
}