package com.github.shiraji.yaemoji.action

import com.github.shiraji.emoji.service.EmojiReader
import com.github.shiraji.yaemoji.domain.EmojiDataManager
import com.intellij.openapi.application.PreloadingActivity
import com.intellij.openapi.application.ex.ApplicationUtil.runWithCheckCanceled
import com.intellij.openapi.progress.ProgressIndicator

class EmojiPreloadingActivity : PreloadingActivity() {
    override fun preload(indicator: ProgressIndicator) {
        EmojiDataManager.emojiList.clear()
        EmojiDataManager.emojiList.addAll(runWithCheckCanceled({
            EmojiReader.loadEmoji()
        }, indicator))
    }
}