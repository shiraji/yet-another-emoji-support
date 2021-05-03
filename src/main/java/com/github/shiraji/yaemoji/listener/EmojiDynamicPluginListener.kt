package com.github.shiraji.yaemoji.listener

import com.github.shiraji.emoji.service.EmojiReader
import com.github.shiraji.yaemoji.domain.EmojiDataManager
import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor

class EmojiDynamicPluginListener : DynamicPluginListener {

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString == "com.github.shiraji.yaemoji" && EmojiDataManager.emojiList.isEmpty()) {
            EmojiDataManager.emojiList.addAll(EmojiReader.loadEmoji())
        }
    }
}