package com.lesincs.simpleread.ui.fragment

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.lesincs.simpleread.R

/**
 * Created by Administrator on 2018/3/31.
 */
class SettingFrag:PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.frag_setting)
    }
}