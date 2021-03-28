package ru.skubatko.dev.hyperskill.case6358

class Case6358

fun main() {
    val lines = Case6358::class.java.getResourceAsStream("/words_with_numbers.txt").bufferedReader().readLines()
    println(lines.filter { it.toIntOrNull() != null }.count())
}
