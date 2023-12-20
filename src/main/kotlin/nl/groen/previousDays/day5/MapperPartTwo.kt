package nl.groen.previousDays.day5

class MapperPartTwo(var name: String) {

    var mapLines = arrayListOf<List<Long>>()
    var longRangeLines = arrayListOf<MapLine>()

    fun determineLowestRange(): LongRange {
        return longRangeLines.minBy {it.resultaat}.range
    }

    fun mapBack(input: Long): Long {
        for (mapLine in longRangeLines) {

            var numberInPreviousMapper = input-(mapLine.resultaat-mapLine.range.first)
            //println("check $input as $numberInPreviousMapper in ${mapLine.range}")
            if (mapLine.range.contains(numberInPreviousMapper)) {
                //println("$input -> $numberInPreviousMapper")
                return numberInPreviousMapper
            }
        }

        //println("$input -> $input")
        return input
    }

}
