package ru.skubatko.dev.hyperskill.project.phonebook.stage1

import java.io.File

class PhoneBookStage1

fun main() {
    val directory = File("/Users/skubatko/Downloads/directory.txt").readLines()
//    val directory = PhoneBookStage1::class.java.getResourceAsStream("/directory.txt").bufferedReader().readLines()
//    val directory = PhoneBookStage1::class.java.getResourceAsStream("/small_directory.txt").bufferedReader().readLines()
    val find = File("/Users/skubatko/Downloads/find.txt").readLines()
//    val find = PhoneBookStage1::class.java.getResourceAsStream("/find.txt").bufferedReader().readLines()
//    val find = PhoneBookStage1::class.java.getResourceAsStream("/small_find.txt").bufferedReader().readLines()

    println("Start searching...")
    val start = System.currentTimeMillis()
    val result = mutableMapOf<String, String>()
    for (person in find) {
        for (line in directory) {
            if (line.contains(person)) {
                result[person] = line.split(" ")[0]
                break
            }
        }
    }
    val timeTaken = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", System.currentTimeMillis() - start)
    println("Found ${result.size} / ${find.size} entries. Time taken: $timeTaken")
}
