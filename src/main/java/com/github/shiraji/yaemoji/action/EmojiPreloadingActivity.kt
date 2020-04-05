package com.github.shiraji.yaemoji.action

import com.github.shiraji.yaemoji.domain.EmojiCompletion
import com.github.shiraji.yaemoji.domain.EmojiDataManager
import com.intellij.openapi.application.PreloadingActivity
import com.intellij.openapi.application.ex.ApplicationUtil.runWithCheckCanceled
import com.intellij.openapi.progress.ProgressIndicator
import java.util.concurrent.Callable

class EmojiPreloadingActivity : PreloadingActivity() {
    override fun preload(indicator: ProgressIndicator) {
        val callable = Callable {
            javaClass.getResourceAsStream("/emojis/emoji.csv").use { inputStream ->
                inputStream.bufferedReader().use { reader ->
                    reader.readLines().map { line ->
                        EmojiCompletion.fromCsv(line)
                    }
                }
            }
        }

        EmojiDataManager.emojiList = runWithCheckCanceled(callable, indicator)
    }
}