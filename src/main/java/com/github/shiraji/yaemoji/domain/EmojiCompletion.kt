package com.github.shiraji.yaemoji.domain

import com.intellij.util.IconUtil
import com.intellij.util.ui.JBUI
import javax.swing.Icon
import javax.swing.JLabel

data class EmojiCompletion(val id: Int, val label: String, val emoji: String, private val hasMultiple: Boolean) {
    val icon: Icon?
        get() {
            if (hasMultiple) return null else return IconUtil.textToIcon(emoji, JLabel(), JBUI.scale(11f))
        }

    companion object {
        fun fromCsv(line: String): EmojiCompletion {
            val parts = line.split("\t")
            val codePoints = parts[1].split(" ").map { Integer.decode(it) }.toIntArray()
            val emoji = String(codePoints, 0, codePoints.size)
            return EmojiCompletion(
                    id = parts[0].toInt(),
                    label = parts[2].replace(" ", "_"),
                    emoji = emoji,
                    hasMultiple = codePoints.size > 1
            )
        }
    }
}