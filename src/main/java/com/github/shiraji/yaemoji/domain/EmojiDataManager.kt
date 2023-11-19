package com.github.shiraji.yaemoji.domain

object EmojiDataManager {
    val emojiList: MutableList<EmojiCompletion> = mutableListOf()

    fun loadEmoji(filePath: String = "/emojis/emoji.csv") {
        val emojis = javaClass.getResourceAsStream(filePath).use { inputStream ->
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