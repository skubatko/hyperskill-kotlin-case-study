package ru.skubatko.dev.hyperskill.project.phonebook

import kotlin.math.sqrt

class PhoneBookStage2

fun main() {
//    val directory = File("/Users/skubatko/Downloads/directory.txt").readLines()
//    val directory = PhoneBookStage2::class.java.getResourceAsStream("/directory.txt").bufferedReader().readLines()
    val directory = PhoneBookStage2::class.java.getResourceAsStream("/small_directory.txt").bufferedReader().readLines()
//    val find = File("/Users/skubatko/Downloads/find.txt").readLines()
//    val find = PhoneBookStage2::class.java.getResourceAsStream("/find.txt").bufferedReader().readLines()
    val find = PhoneBookStage2::class.java.getResourceAsStream("/small_find.txt").bufferedReader().readLines()

    println("Start searching (linear search)...")
    val start = System.currentTimeMillis()
    val result = linearSearch(find, directory)
    val linearSearchTime = System.currentTimeMillis() - start
    val timeTaken = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", linearSearchTime)
    println("Found ${result.size} / ${find.size} entries. Time taken: $timeTaken")
    println()

    bubbleSortJumpSearch(find, directory, linearSearchTime)
}

private fun linearSearch(
    find: List<String>,
    directory: List<String>
): Map<String, String> {
    val result = mutableMapOf<String, String>()
    for (person in find) {
        for (line in directory) {
            if (line.contains(person)) {
                result[person] = line.split(" ")[0]
                break
            }
        }
    }

    return result
}

private fun bubbleSortJumpSearch(
    find: List<String>,
    directory: List<String>,
    linearSearchTime: Long
) {
    println("Start searching (bubble sort + jump search)...")
    val start = System.currentTimeMillis()


    val (sorted, isSuccessful) = bubbleSort(directory, linearSearchTime)
    val sortingTime = System.currentTimeMillis() - start

    var result = mutableMapOf<String, String>()
    if (isSuccessful) {
        for (person in find) {
            result[person] = jumpSearch(sorted, person)
        }
    } else {
        val data = linearSearch(find, directory)
        result = data.toMutableMap()
    }

    val totalTime = System.currentTimeMillis() - start
    val totalTimeTaken = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", totalTime)
    println("Found ${result.size} / ${find.size} entries. Time taken: $totalTimeTaken")

    val sortingTimeTaken = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", sortingTime)
    if (isSuccessful) {
        println("Sorting time: $sortingTimeTaken")
    } else {
        println("Sorting time: $sortingTimeTaken - STOPPED, moved to linear search")
    }

    val searchingTimeTaken = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", totalTime - sortingTime)
    println("Searching time: $searchingTimeTaken")
}

private fun jumpSearch(sorted: List<String>, person: String): String {
    val step = sqrt(sorted.size.toDouble()).toInt()
    for (i in 0..sorted.lastIndex step step) {
        val record = sorted[i]

        if (record.contains(person)) {
            return record.split(" ")[0]
        }

        if (person < personRecordName(record)) {
            for (j in (i - 1) downTo (i - step)) {
                if (sorted[j].contains(person)) {
                    return sorted[j].split(" ")[0]
                }
            }
            return ""
        }
    }
    return ""
}

private fun bubbleSort(
    directory: List<String>,
    linearSearchTime: Long
): Pair<List<String>, Boolean> {
    val timeLimit = linearSearchTime * 10

    val result = directory.toMutableList()

    val start = System.currentTimeMillis()
    for (pos in result.lastIndex downTo 1) {
        for (i in 0 until pos) {
            if (personRecordCompare(result[i], result[i + 1]) > 0) {
                switch(result, i)
                val elapsed = System.currentTimeMillis() - start
                if (timeLimit < elapsed) {
                    return Pair(directory, false)
                }
            }
        }
    }

    return Pair(result.toList(), true)
}

private fun personRecordCompare(record1: String, record2: String): Int {
    return personRecordName(record1).compareTo(personRecordName(record2))
}

private fun personRecordName(record: String): String {
    val idx = record.indexOf(" ")
    return record.substring(idx + 1)
}

private fun switch(list: MutableList<String>, idx: Int) {
    val value = list[idx]
    list[idx] = list[idx + 1]
    list[idx + 1] = value
}
