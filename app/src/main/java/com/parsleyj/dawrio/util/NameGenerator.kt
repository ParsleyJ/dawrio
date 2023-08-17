package com.parsleyj.dawrio.util

object NameGenerator {
    //TODO better implementation that takes into account only used names
    private var counter = 0
    fun newName(startingWith: String):String = "$startingWith${counter++}"
}