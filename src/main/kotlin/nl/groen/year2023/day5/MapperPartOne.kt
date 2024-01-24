package nl.groen.year2023.day5

class MapperPartOne(var name: String) {

    fun applyMap(input: ArrayList<Long>): ArrayList<Long> {

        var result = arrayListOf<Long>()
        for (number in input) {

            result.add(map(number))
        }

        println("end of mapper $name")
        return result
    }

    private fun map(number: Long): Long {
        for (line in mapLines) {
            if (LongRange(line[1], line[1] + line[2] - 1).contains(number)) {
                val result = line[0] - line[1] + number
                println("map $number to $result")
                return result
            }
        }

        println("keep $number to $number")
        return number
    }

    var mapLines = arrayListOf<List<Long>>()

}
