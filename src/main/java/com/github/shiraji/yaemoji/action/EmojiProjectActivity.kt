package com.github.shiraji.yaemoji.action

import com.github.shiraji.yaemoji.domain.EmojiDataManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class EmojiProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        EmojiDataManager.clearEmoji()
        EmojiDataManager.loadEmoji()
    }
}