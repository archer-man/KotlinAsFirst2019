@file:Suppress("UNUSED_PARAMETER")

package lesson2.task1

import lesson1.task1.discriminant
import kotlin.math.*

/**
 * Пример
 *
 * Найти число корней квадратного уравнения ax^2 + bx + c = 0
 */
fun quadraticRootNumber(a: Double, b: Double, c: Double): Int {
    val discriminant = discriminant(a, b, c)
    return when {
        discriminant > 0.0 -> 2
        discriminant == 0.0 -> 1
        else -> 0
    }
}

/**
 * Пример
 *
 * Получить строковую нотацию для оценки по пятибалльной системе
 */
fun gradeNotation(grade: Int): String = when (grade) {
    5 -> "отлично"
    4 -> "хорошо"
    3 -> "удовлетворительно"
    2 -> "неудовлетворительно"
    else -> "несуществующая оценка $grade"
}

/**
 * Пример
 *
 * Найти наименьший корень биквадратного уравнения ax^4 + bx^2 + c = 0
 */
fun minBiRoot(a: Double, b: Double, c: Double): Double {
    // 1: в главной ветке if выполняется НЕСКОЛЬКО операторов
    if (a == 0.0) {
        if (b == 0.0) return Double.NaN // ... и ничего больше не делать
        val bc = -c / b
        if (bc < 0.0) return Double.NaN // ... и ничего больше не делать
        return -sqrt(bc)
        // Дальше функция при a == 0.0 не идёт
    }
    val d = discriminant(a, b, c)   // 2
    if (d < 0.0) return Double.NaN  // 3
    // 4
    val y1 = (-b + sqrt(d)) / (2 * a)
    val y2 = (-b - sqrt(d)) / (2 * a)
    val y3 = max(y1, y2)       // 5
    if (y3 < 0.0) return Double.NaN // 6
    return -sqrt(y3)           // 7
}

/**
 * Простая
 *
 * Мой возраст. Для заданного 0 < n < 200, рассматриваемого как возраст человека,
 * вернуть строку вида: «21 год», «32 года», «12 лет».
 */
fun ageDescription(age: Int): String {
    val lastDigit: Int = age % 10
    if (age !in 10..20 && age !in 110..120) {
        if (lastDigit == 1) return "$age год"
        if (lastDigit in 2..4) return "$age года"
    }
    return "$age лет"
}

/**
 * Простая
 *
 * Путник двигался t1 часов со скоростью v1 км/час, затем t2 часов — со скоростью v2 км/час
 * и t3 часов — со скоростью v3 км/час.
 * Определить, за какое время он одолел первую половину пути?
 */
fun timeForHalfWay(
    t1: Double, v1: Double,
    t2: Double, v2: Double,
    t3: Double, v3: Double
): Double {
    val halfOfDistance = (v1 * t1 + v2 * t2 + v3 * t3) / 2
    val firstAndSecondDistance = (v1 * t1 + v2 * t2)
    if (halfOfDistance <= firstAndSecondDistance) {
        return if (halfOfDistance <= v1 * t1) halfOfDistance / v1 else (halfOfDistance - v1 * t1) / v2 + t1
    }
    return (halfOfDistance - v1 * t1 - v2 * t2) / v3 + t1 + t2
}

/**
 * Простая
 *
 * Нa шахматной доске стоят черный король и две белые ладьи (ладья бьет по горизонтали и вертикали).
 * Определить, не находится ли король под боем, а если есть угроза, то от кого именно.
 * Вернуть 0, если угрозы нет, 1, если угроза только от первой ладьи, 2, если только от второй ладьи,
 * и 3, если угроза от обеих ладей.
 * Считать, что ладьи не могут загораживать друг друга
 */
fun whichRookThreatens(
    kingX: Int, kingY: Int,
    rookX1: Int, rookY1: Int,
    rookX2: Int, rookY2: Int
): Int {
    if (kingX == rookX1 || kingX == rookX2 || kingY == rookY1 || kingY == rookY2) { //check, whether there is a threat at all
        if ((kingX == rookX1 || kingY == rookY1) && (kingX != rookX2 && kingY != rookY2)) return 1 //return 1 if the rook1 is a threat
        if ((kingX == rookX2 || kingY == rookY2) && (kingX != rookX1 && kingY != rookY1)) return 2 //return 2 if the rook2 is a threat
        if ((kingX == rookX1 || kingY == rookY1) && (kingX == rookX2 || kingY == rookY2)) return 3 //return 3 if rook1 and rook2 are threats
    }
    return 0
}

/**
 * Простая
 *
 * На шахматной доске стоят черный король и белые ладья и слон
 * (ладья бьет по горизонтали и вертикали, слон — по диагоналям).
 * Проверить, есть ли угроза королю и если есть, то от кого именно.
 * Вернуть 0, если угрозы нет, 1, если угроза только от ладьи, 2, если только от слона,
 * и 3, если угроза есть и от ладьи и от слона.
 * Считать, что ладья и слон не могут загораживать друг друга.
 */
fun rookOrBishopThreatens(
    kingX: Int, kingY: Int,
    rookX: Int, rookY: Int,
    bishopX: Int, bishopY: Int
): Int {
    val horizontalDistance: Int = kingX - bishopX
    val x: Int = abs(horizontalDistance)
    val verticalDistance: Int = kingY - bishopY
    val y: Int = abs(verticalDistance)
    if (kingX == rookX || kingY == rookY || x == y) { //check, whether there is a threat at all
        if ((kingX == rookX || kingY == rookY) && (x != y)) return 1 //return 1 if the rook is a threat
        if ((x == y) && (kingX != rookX && kingY != rookY)) return 2 //return 2 if the bishop is a threat
        if ((kingX == rookX || kingY == rookY) && (x == y)) return 3 //return 3 if rook1 and bishop are threats
    }
    return 0
}

/**
 * Простая
 *
 * Треугольник задан длинами своих сторон a, b, c.
 * Проверить, является ли данный треугольник остроугольным (вернуть 0),
 * прямоугольным (вернуть 1) или тупоугольным (вернуть 2).
 * Если такой треугольник не существует, вернуть -1.
 */
fun triangleKind(a: Double, b: Double, c: Double): Int {
    val max = maxOf(a, b, c)
    val min = minOf(a, b, c)
    val middle = a + b + c - max - min
    if (max < middle + min) {
        if (max.pow(2) < middle.pow(2) + min.pow(2)) return 0
        if (max.pow(2) > middle.pow(2) + min.pow(2)) return 2
        else return 1
    }
    return -1
}

/**
 * Средняя
 *
 * Даны четыре точки на одной прямой: A, B, C и D.
 * Координаты точек a, b, c, d соответственно, b >= a, d >= c.
 * Найти длину пересечения отрезков AB и CD.
 * Если пересечения нет, вернуть -1.
 */
fun segmentLength(a: Int, b: Int, c: Int, d: Int): Int {
    if (b >= a && d >= c && d > a && b >= c) {
        val max: Int = maxOf(b, d)
        val min: Int = minOf(a, c)
        if (a == min && b == max) return d - c
        if (c == min && d == max) return b - a
        if (a == min && d == max) return abs(b - c)
        if (c == min && b == max) return abs(d - a)
    }
    return -1
}
