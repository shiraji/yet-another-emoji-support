package com.github.shiraji.emoji.service

import com.github.shiraji.yaemoji.domain.EmojiCompletion

object EmojiReader {
    fun loadEmoji(): List<EmojiCompletion> {
        javaClass.getResourceAsStream("/emojis/emoji.csv").use { inputStream ->
            return inputStream?.bufferedReader()?.use { reader ->
                reader.readLines().map { line ->
                    EmojiCompletion.fromCsv(line)
                }
            } ?: emptyList()
        }
    }
}