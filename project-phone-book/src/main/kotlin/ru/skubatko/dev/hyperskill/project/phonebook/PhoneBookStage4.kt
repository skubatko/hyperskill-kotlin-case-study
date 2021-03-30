package ru.skubatko.dev.hyperskill.project.phonebook

import kotlin.math.sqrt

class PhoneBookStage4

fun main() {
//    val directory = File("/Users/skubatko/Downloads/directory.txt").readLines()
    val directory = PhoneBookStage4::class.java.getResourceAsStream("/directory.txt").bufferedReader().readLines()
//    val directory = PhoneBookStage4::class.java.getResourceAsStream("/small_directory.txt").bufferedReader().readLines()
//    val find = File("/Users/skubatko/Downloads/find.txt").readLines()
    val find = PhoneBookStage4::class.java.getResourceAsStream("/find.txt").bufferedReader().readLines()
//    val find = PhoneBookStage4::class.java.getResourceAsStream("/small_find.txt").bufferedReader().readLines()

    val linearSearchTime = pureLinearSearch(find, directory)
    println()

    bubbleSortJumpSearch(find, directory, linearSearchTime)
    println()

    quickSortBinarySearch(find, directory)
    println()

    hashTable(find, directory)
}

private fun pureLinearSearch(
    find: List<String>,
    directory: List<String>
): Long {
    println("Start searching (linear search)...")
    val start = System.currentTimeMillis()
    val result = linearSearch(find, directory)
    val linearSearchTime = System.currentTimeMillis() - start
    val timeTaken = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", linearSearchTime)
    println("Found ${result.size} / ${find.size} entries. Time taken: $timeTaken")
    return linearSearchTime
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
                swap(result, i)
                val elapsed = System.currentTimeMillis() - start
                if (timeLimit < elapsed) {
                    return Pair(directory, false)
                }
            }
        }
    }

    return Pair(result.toList(), true)
}

private fun swap(list: MutableList<String>, idx: Int) {
    val value = list[idx]
    list[idx] = list[idx + 1]
    list[idx + 1] = value
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
                    return personNumber(sorted, j)
                }
            }
            return ""
        }
    }
    return ""
}

private fun quickSortBinarySearch(find: List<String>, directory: List<String>) {
    println("Start searching (quick sort + binary search)...")
    val start = System.currentTimeMillis()

    val sorted = quickSort(directory)
    val sortingTime = System.currentTimeMillis() - start

    val result = mutableMapOf<String, String>()
    for (person in find) {
        result[person] = binarySearch(sorted, person)
    }

    val totalTime = System.currentTimeMillis() - start
    val totalTimeTaken = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", totalTime)
    println("Found ${result.size} / ${find.size} entries. Time taken: $totalTimeTaken")

    val sortingTimeTaken = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", sortingTime)
    println("Sorting time: $sortingTimeTaken")

    val searchingTimeTaken = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", totalTime - sortingTime)
    println("Searching time: $searchingTimeTaken")
}

private fun quickSort(directory: List<String>): List<String> {
    val result = directory.toMutableList()
    quickSort(result, 0, result.lastIndex)
    return result.toList()
}

private fun quickSort(list: MutableList<String>, left: Int, right: Int) {
    if (left < right) {
        val pivotIndex: Int = partition(list, left, right) // the pivot is already on its place
        quickSort(list, left, pivotIndex - 1) // sort the left subarray
        quickSort(list, pivotIndex + 1, right) // sort the right subarray
    }
}

private fun partition(list: MutableList<String>, left: Int, right: Int): Int {
    val pivot = list[right] // choose the rightmost element as the pivot
    var partitionIndex = left // the first element greater than the pivot

    /* move large values into the right side of the array */for (i in left until right) {
        if (personRecordCompare(list[i], pivot) <= 0) { // may be used '<' as well
            swap(list, i, partitionIndex)
            partitionIndex++
        }
    }
    swap(list, partitionIndex, right) // put the pivot on a suitable position
    return partitionIndex
}

private fun swap(list: MutableList<String>, i: Int, j: Int) {
    val temp = list[i]
    list[i] = list[j]
    list[j] = temp
}

private fun binarySearch(sorted: List<String>, person: String): String {
    var left = 0
    var right = sorted.lastIndex
    while (left <= right) {
        val mid = left + (right - left) / 2 // the index of the middle element
        if (sorted[mid].contains(person)) return personNumber(sorted, mid)
        else if (person < personRecordName(sorted[mid])) right = mid - 1
        else left = mid + 1
    }
    return "" // the element is not found
}

private fun personRecordCompare(record1: String, record2: String): Int =
    personRecordName(record1).compareTo(personRecordName(record2))

private fun personRecordName(record: String): String {
    val idx = record.indexOf(" ")
    return record.substring(idx + 1)
}

private fun personNumber(list: List<String>, idx: Int) =
    list[idx].split(" ")[0]

private fun hashTable(find: List<String>, directory: List<String>) {
    println("Start searching (hash table)...")
    val start = System.currentTimeMillis()

    val hashTable = directory.associateBy(::personRecordName) { it.split(" ")[0] }
    val creatingTime = System.currentTimeMillis() - start

    val result = hashTable.filter { find.contains(it.key) }

    val totalTime = System.currentTimeMillis() - start
    val totalTimeTaken = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", totalTime)
    println("Found ${result.size} / ${find.size} entries. Time taken: $totalTimeTaken")

    val creatingTimeTaken = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", creatingTime)
    println("Creating time: $creatingTimeTaken")

    val searchingTimeTaken = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", totalTime - creatingTime)
    println("Searching time: $searchingTimeTaken")
}
