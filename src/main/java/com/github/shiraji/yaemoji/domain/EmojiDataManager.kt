package com.github.shiraji.yaemoji.domain

object EmojiDataManager {
    val emojiList: MutableList<EmojiCompletion> = mutableListOf()

    fun loadEmoji() {
        val emojis = javaClass.getResourceAsStream("/emojis/emoji.csv").use { inputStream ->
            inputStream?.bufferedReader()?.use { reader ->
                reader.readLines().map { line ->
                    EmojiCompletion.fromCsv(line)
                }
            } ?: emptyList()
        }
        emojiList.addAll(emojis)
    }

    fun clearEmoji() {
        emojiList.clear()
    }

    fun hasEmoji(): Boolean {
        return emojiList.isNotEmpty()
    }
}