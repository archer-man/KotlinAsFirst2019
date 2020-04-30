@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import lesson1.task1.sqr

/**
 * Класс "комплексое число".
 *
 * Общая сложность задания -- лёгкая.
 * Объект класса -- комплексное число вида x+yi.
 * Про принципы работы с комплексными числами см. статью Википедии "Комплексное число".
 *
 * Аргументы конструктора -- вещественная и мнимая часть числа.
 */
class Complex(val re: Double, val im: Double) {

    /**
     * Конструктор из вещественного числа
     */
    constructor(x: Double) : this(x, 0.0)

    /**
     * Конструктор из строки вида x+yi
     */
    constructor(s: String) : this(
        s.split(Regex("""\b[+|-]"""))[0].toDouble(),
        s.replace("i", "").split(Regex("""\b[+|-]"""))[1].toDouble() *
                if (Regex("""\b[-]\d[i]""").containsMatchIn(s)) -1 else 1
    )


    /**
     * Сложение.
     */
    operator fun plus(other: Complex): Complex = Complex(other.re + re, other.im + im)

    /**
     * Смена знака (у обеих частей числа)
     */
    operator fun unaryMinus(): Complex = Complex(re.unaryMinus(), im.unaryMinus())

    /**
     * Вычитание
     */
    operator fun minus(other: Complex): Complex = Complex(re - other.re, im - other.im)

    /**
     * Умножение
     */
    operator fun times(other: Complex): Complex =
        Complex((other.re * re) - (other.im * im), (other.re * im) + (re * other.im))

    /**
     * Деление
     */
    operator fun div(other: Complex): Complex = Complex(
        (other.re * re + other.im * im) / (sqr(other.re) + sqr(other.im)),
        (other.re * im - re * other.im) / (sqr(other.re) + sqr(other.im))
    )

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = other is Complex && (re == other.re && im == other.im)

    /**
     * Преобразование в строку
     */
    override fun toString(): String {
        val reIntStr = re.toInt().toString()
        val imIntStr = im.toInt().toString()
        if (im < 0) return reIntStr + imIntStr + "i" else return reIntStr + "+" + imIntStr + "i"
    }
}