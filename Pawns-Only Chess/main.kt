
val desk =
        mutableListOf(
                mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0),
                mutableListOf<Int>(-1, -1, -1, -1, -1, -1, -1, -1),
                mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0),
                mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0),
                mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0),
                mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0),
                mutableListOf<Int>(1, 1, 1, 1, 1, 1, 1, 1),
                mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0)
        )

val lastMove = mutableListOf("", "")
var numberOfWhitePawn = 8
var numberOfBlackPawn = 8

fun main() {
    var step = 0
    val playersName = mutableListOf<String>()
    println("Pawns-Only Chess")
    println("First Player's name:")
    playersName.add(readln())
    println("Second Player's name:")
    playersName.add(readln())
    printDesk()
    println("${playersName[0]}'s turn:")
    var move = readln()
    while (move != "exit") {
        println(step)
        var isValid = if (move[0] == move[2]) isWalkable(move, step % 2) else isEatable(move, step % 2)
        if(isValid == "true") {
            updateDesk(move, step % 2)
            lastMove[step % 2] = move
            if (isWin(move, step % 2) || isStalemate((step + 1) % 2)) break
            step += 1
        }
        else println("$isValid")
        println("${playersName[step % 2]}'s turn:")
        move = readln()
    }
    print("Bye!")
}

fun isWalkable(move: String, who: Int): String {
    val moveRegex = Regex("[a-h][1-8][a-h][1-8]")
    val start = move.substring(0, 2)
    val target = move.substring(2)
    val color = if (who == 0) 1 else -1
    if (move.matches(moveRegex) == false) return "Invalid Input"
    if (desk[8 - start.substring(1).toInt()][start.first().toInt() - 97] != color)
            return "No ${if (color == 1) "white" else "black"} pawn at $start"
    if (desk[8 - target.substring(1).toInt()][target.first().toInt() - 97] != 0)
            return "Invalid Input"
    if (target.first() != start.first()) return "Invalid Input"
    if (target.last() != start.last() + color) {
        if (start.last() != '2' && start.last() != '7') return "Invalid Input"
        else if (target.last() != start.last() + color * 2) return "Invalid Input"
    }

    return "true"
}

fun isEatable(move: String, who: Int): String {
    val color = if (who == 0) 1 else -1
    val moveRegex = Regex("[a-h][1-8][a-h][1-8]")
    val start = move.substring(0, 2)
    val target = move.substring(2)
    var isEnPassant = false
    val targetsMove = lastMove[lastMove.size - 1 - who]
    if (move.matches(moveRegex) == false) return "Invalid Input1"
    if (Math.abs(start.first() - target.first()) != 1) return "Invalid Input2"
    if (desk[8 - target.substring(1).toInt()][target.first().toInt() - 97] != color * -1) {
        if (desk[8 - start.substring(1).toInt()][target.first().toInt() - 97] == color * -1 &&
                        Math.abs(targetsMove[1] - targetsMove[3]) == 2
        )
                isEnPassant = true
        else return "Invalid Input3"
    }

    if (isEnPassant) {
        desk[8 - targetsMove[3].toString().toInt()][targetsMove[2].toInt() - 97] = 0
    }
    if (who == 0) numberOfBlackPawn-- else numberOfWhitePawn--

    return "true"
}

fun isWin(move: String, who: Int): Boolean {
    if (who == 0 && move.last() == '8' || numberOfBlackPawn == 0) {
        println("White wins!")
        return true
    }
    if (who == 1 && move.last() == '1' || numberOfWhitePawn == 0) {
        println("Black wins!")
        return true
    }
    return false
}

fun isStalemate(who: Int): Boolean {
    val color = if (who == 0) 1 else -1
    var x1 = 'a'
    var x2 = 'a'
    var y1 = 8
    var y2 = 8
    for (i in 0..7) {
        if (desk[i].contains(color)) {
            for (j in 0..7) {
                x1 = 'a' + j
                x2 = x1 - 1
                y1 = 8 - i
                y2 = y1 + color
                if (desk[i][j] == color) {
                    if (j == 0 && isCanWalk("$x1$y1${x2 + 1}$y2", who) ||
                                    isCanEat("$x1$y1${x2 + 2}$y2", who)
                    )
                            return false
                    else if (j == 7 && isCanEat("$x1$y1$x2$y2", who) ||
                                    isCanWalk("$x1$y1${x2 + 1}$y2", who)
                    )
                            return false
                    else if (isCanEat("$x1$y1$x2$y2", who) ||
                                    isCanWalk("$x1$y1${x2 + 1}$y2", who) ||
                                    isCanEat("$x1$y1${x2 + 2}$y2", who)
                    )
                            return false
                }
            }
        }
    }
    println("Stalemate!")
    return true
}

fun isCanWalk(move: String, who: Int): Boolean {
    val moveRegex = Regex("[a-h][1-8][a-h][1-8]")
    val start = move.substring(0, 2)
    val target = move.substring(2)
    val color = if (who == 0) 1 else -1 
    if (move.matches(moveRegex) == false) return false
    if (desk[8 - start.substring(1).toInt()][start.first().toInt() - 97] != color) return false
    if (desk[8 - target.substring(1).toInt()][target.first().toInt() - 97] != 0) return false
    if (target.first() != start.first()) return false
    if (target.last() != start.last() + color) {
        if (start.last() != '2' && start.last() != '7') return false
        else if (target.last() != start.last() + color * 2) return false
    }

    return true
}

fun isCanEat(move: String, who: Int): Boolean {
    val color = if (who == 0) 1 else -1
    val moveRegex = Regex("[a-h][1-8][a-h][1-8]")
    val start = move.substring(0, 2)
    val target = move.substring(2)
    val targetsMove = lastMove[lastMove.size - 1 - who]
    if (move.matches(moveRegex) == false) return false
    if (Math.abs(start.first() - target.first()) != 1) return false
    if (desk[8 - target.substring(1).toInt()][target.first().toInt() - 97] != color * -1) {
        if (desk[8 - start.substring(1).toInt()][target.first().toInt() - 97] != color * -1 &&
                        Math.abs(targetsMove[1] - targetsMove[3]) != 2
        )
                return false
    }

    return true
}

fun updateDesk(move: String, who: Int) {
    val color = if (who == 0) 1 else -1
    val start = move.substring(0, 2)
    val target = move.substring(2)
    desk[8 - start.substring(1).toInt()][start.first().toInt() - 97] = 0
    desk[8 - target.substring(1).toInt()][target.first().toInt() - 97] = color
    printDesk()
}

fun printDesk() {
    for (i in 0..7) {
        println("  +---+---+---+---+---+---+---+---+")
        print("${8 - i} |")
        for (j in 0..7) {
            if (desk[i][j] == -1) {
                print(" B |")
            } else if (desk[i][j] == 1) {
                print(" W |")
            } else print("   |")
        }
        println()
    }
    println("  +---+---+---+---+---+---+---+---+")
    println("    a   b   c   d   e   f   g   h")
}
