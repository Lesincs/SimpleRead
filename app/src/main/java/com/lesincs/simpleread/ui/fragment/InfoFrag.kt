package com.lesincs.simpleread.ui.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.widget.Toast
import com.lesincs.simpleread.R
import com.lesincs.simpleread.util.ClipboardUtil

/**
 * Created by Administrator on 2017/12/29.
 */
class InfoFrag : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        addPreferencesFromResource(R.xml.frag_about)

        findPreference("qq_mail").setOnPreferenceClickListener {

            val uri = Uri.parse("mailto:" + getString(R.string.fox_mail_summary))
            val intent = Intent(Intent.ACTION_SENDTO, uri)

            /*判断是够有能匹配该隐式intent的app*/
            if (context?.packageManager?.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                startActivity(intent)
            } else {
                toastMsg(getString(R.string.no_match_app))
            }

            true //是否消费点击
        }

        findPreference("google_mail").setOnPreferenceClickListener {

            val uri = Uri.parse("mailto:" + getString(R.string.google_mail_summary))
            val intent = Intent(Intent.ACTION_SENDTO, uri)

            if (context?.packageManager?.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                startActivity(intent)
            } else {
                toastMsg(getString(R.string.no_match_app))
            }

            true
        }

        findPreference("donate_author").setOnPreferenceClickListener {

            ClipboardUtil.copyTextToClipboard(getString(R.string.aliPay_account))
            toastMsg(getString(R.string.aliPay_account_copy_success))
            true
        }

        findPreference("give_a_five_star").setOnPreferenceClickListener {

            val uri = Uri.parse("market://details?id=" + context?.packageName)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            if (context?.packageManager?.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                startActivity(intent)
            } else {
                toastMsg(getString(R.string.no_match_app))
            }

            true
        }

        findPreference("open_source").setOnPreferenceClickListener {

            val url = getText(R.string.open_source_summary).toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            true
        }
    }

    private fun toastMsg(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}