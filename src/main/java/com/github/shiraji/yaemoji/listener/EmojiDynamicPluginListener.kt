package com.github.shiraji.yaemoji.listener

import com.github.shiraji.yaemoji.domain.EmojiDataManager
import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor

class EmojiDynamicPluginListener : DynamicPluginListener {
    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString == "com.github.shiraji.yaemoji" && !EmojiDataManager.hasEmoji()) {
            EmojiDataManager.loadEmoji()
        }
    }
}