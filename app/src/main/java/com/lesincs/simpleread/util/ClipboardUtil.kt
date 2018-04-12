package com.lesincs.simpleread.util

import android.content.ClipData
import android.content.Context
import com.lesincs.simpleread.base.App

/**
 * Created by Administrator on 2017/12/18.
 */
class ClipboardUtil {
    companion object {

        fun copyTextToClipboard(text: String) {
            val manager = App.sContext.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            manager.primaryClip = ClipData.newPlainText("text", text)
        }

    }
}