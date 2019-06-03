package com.github.shiraji.yaemoji.utils

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.StandardPatterns

infix fun <T> ElementPattern<T>.or(rhs: ElementPattern<T>) = StandardPatterns.or(this, rhs)