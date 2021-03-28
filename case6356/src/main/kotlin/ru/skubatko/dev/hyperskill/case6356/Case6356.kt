package ru.skubatko.dev.hyperskill.case6356

class Case6356

fun main() {
    val lines = Case6356::class.java.getResourceAsStream("/words_sequence.txt").bufferedReader().readLines()
    println(lines.map { it.length }.maxByOrNull { it })
}
