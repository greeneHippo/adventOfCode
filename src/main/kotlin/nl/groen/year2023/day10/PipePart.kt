package nl.groen.year2023.day10

enum class PipePart(val symbol: String, val outputSymbol: String, val directionsTo: List<Direction>, val directionsFrom: List<Direction>) {

    START("S", "S",  listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST), listOf()),
    VERTICAL("|", "║", listOf(Direction.NORTH, Direction.SOUTH), listOf(Direction.NORTH, Direction.SOUTH)),
    HORIZONTAL("-", "═", listOf(Direction.EAST, Direction.WEST), listOf(Direction.EAST, Direction.WEST)),
    BEND_NE("L", "╚", listOf(Direction.NORTH, Direction.EAST), listOf(Direction.SOUTH, Direction.WEST)),
    BEND_NW("J", "╝", listOf(Direction.NORTH, Direction.WEST), listOf(Direction.SOUTH, Direction.EAST)),
    BEND_SW("7", "╗", listOf(Direction.WEST, Direction.SOUTH), listOf(Direction.EAST, Direction.NORTH)),
    BEND_SE("F", "╔", listOf(Direction.EAST, Direction.SOUTH), listOf(Direction.WEST, Direction.NORTH)),
    GROUND(".", ".", listOf(), listOf());

    companion object {
        fun get(input: Char): PipePart {
            return entries.firstOrNull { part -> input.uppercase() == part.symbol}!!
        }
    }

    enum class Direction() {
        NORTH, EAST, SOUTH, WEST
    }
}
