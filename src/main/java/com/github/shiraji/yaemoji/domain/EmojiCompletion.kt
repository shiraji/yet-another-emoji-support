package com.github.shiraji.yaemoji.domain

import com.intellij.util.IconUtil
import com.intellij.util.ui.JBUI
import javax.swing.Icon
import javax.swing.JLabel

data class EmojiCompletion(val id: Int, val label: String, val emoji: String, val icon: Icon?) {
    companion object {
        fun fromCsv(line: String): EmojiCompletion {
            val parts = line.split("\t")
            val codePoints = parts[1].split(" ").map { Integer.decode(it) }.toIntArray()
            val emoji = String(codePoints, 0, codePoints.size)
            val icon = if (codePoints.size == 1) IconUtil.textToIcon(emoji, JLabel(), JBUI.scale(11f)) else null

            return EmojiCompletion(
                    id = parts[0].toInt(),
                    label = parts[2].replace(" ", "_"),
                    emoji = emoji,
                    icon = icon
            )
        }
    }
}