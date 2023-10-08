package com.github.shiraji.yaemoji.action

import com.github.shiraji.yaemoji.domain.EmojiCompletion
import com.github.shiraji.yaemoji.domain.EmojiDataManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class EmojiProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val result = javaClass.getResourceAsStream("/emojis/emoji.csv").use { inputStream ->
            inputStream?.bufferedReader().use { reader ->
                reader?.readLines()?.map { line ->
                    EmojiCompletion.fromCsv(line)
                }
            }
        }
        EmojiDataManager.emojiList = result ?: emptyList()
    }
}