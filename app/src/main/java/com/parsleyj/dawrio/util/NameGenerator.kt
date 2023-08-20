package com.parsleyj.dawrio.util

object NameGenerator {
    private val counters: MutableMap<String, Int> = mutableMapOf()
    fun newName(startingWith: String): String {
        val next = counters[startingWith] ?: 0
        counters[startingWith] = next + 1
        return "${startingWith}_${next}"
    }
}