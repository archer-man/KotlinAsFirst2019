@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import kotlin.math.pow

/**
 * Класс "полином с вещественными коэффициентами".
 *
 * Общая сложность задания -- сложная.
 * Объект класса -- полином от одной переменной (x) вида 7x^4+3x^3-6x^2+x-8.
 * Количество слагаемых неограничено.
 *
 * Полиномы можно складывать -- (x^2+3x+2) + (x^3-2x^2-x+4) = x^3-x^2+2x+6,
 * вычитать -- (x^3-2x^2-x+4) - (x^2+3x+2) = x^3-3x^2-4x+2,
 * умножать -- (x^2+3x+2) * (x^3-2x^2-x+4) = x^5+x^4-5x^3-3x^2+10x+8,
 * делить с остатком -- (x^3-2x^2-x+4) / (x^2+3x+2) = x-5, остаток 12x+16
 * вычислять значение при заданном x: при x=5 (x^2+3x+2) = 42.
 *
 * В конструктор полинома передаются его коэффициенты, начиная со старшего.
 * Нули в середине и в конце пропускаться не должны, например: x^3+2x+1 --> Polynom(1.0, 2.0, 0.0, 1.0)
 * Старшие коэффициенты, равные нулю, игнорировать, например Polynom(0.0, 0.0, 5.0, 3.0) соответствует 5x+3
 */
class Polynom(vararg coeffs: Double) {

    var coeffsArray = doubleArrayOf(*coeffs).toMutableList()

    /**
     * Геттер: вернуть значение коэффициента при x^i
     */
    fun coeff(i: Int): Double = coeffsArray[coeffsArray.size - i - 1]

    /**
     * Расчёт значения при заданном x
     */
    fun getValue(x: Double): Double {
        var result = 0.0
        var coeffPower = coeffsArray.size
        for (coefficient in coeffsArray) {
            coeffPower -= 1
            result += x.pow(coeffPower) * coefficient
        }
        return result
    }

    /**
     * Степень (максимальная степень x при ненулевом слагаемом, например 2 для x^2+x+1).
     *
     * Степень полинома с нулевыми коэффициентами считать равной 0.
     * Слагаемые с нулевыми коэффициентами игнорировать, т.е.
     * степень 0x^2+0x+2 также равна 0.
     */
    fun degree(): Int {
        for ((index, value) in coeffsArray.withIndex()) {
            if (value != 0.0) return (coeffsArray.size - index - 1) else continue
        }
        return 0
    }

    /**
     * Сложение
     */
    operator fun plus(other: Polynom): Polynom {
        val summand1 = this.flattener(other).first
        val summand2 = this.flattener(other).second
        val answer = this.flattener(other).third
        for (i in 0 until summand1.coeffsArray.size) {
            answer.coeffsArray[i] = summand1.coeffsArray[i] + summand2.coeffsArray[i]
        }
        return answer
    }

    /**
     * Смена знака (при всех слагаемых)
     */
    operator fun unaryMinus(): Polynom {
        for ((index, value) in this.coeffsArray.withIndex()) {
            this.coeffsArray[index] = value * -1
        }
        return this
    }

    /**
     * Вычитание
     */
    operator fun minus(other: Polynom): Polynom {
        val minuend = this.flattener(other).first
        val subtrahend = this.flattener(other).second
        val answer = this.flattener(other).third
        for (i in 0 until answer.coeffsArray.size) {
            answer.coeffsArray[i] = minuend.coeffsArray[i] - subtrahend.coeffsArray[i]
        }
        return answer
    }

    /**
     * Умножение
     */
    operator fun times(other: Polynom): Polynom {
        val maxItem = Polynom()
        val minItem = Polynom()
        val maxPower = maxOf(this.coeffsArray.size, other.coeffsArray.size) - 1
        var indexCheck = 0
        val answer = Polynom()
        if (minOf(this.coeffsArray.size, other.coeffsArray.size) == this.coeffsArray.size) {
            maxItem.coeffsArray.addAll(other.coeffsArray)
            minItem.coeffsArray.addAll(this.coeffsArray)
        } else {
            maxItem.coeffsArray.addAll(this.coeffsArray)
            minItem.coeffsArray.addAll(other.coeffsArray)
        }
        answer.coeffsArray.addAll(maxItem.coeffsArray)
        answer.coeffsArray.fill(0.0)
        while (indexCheck < minItem.coeffsArray.size) {
            if (minItem.coeffsArray[indexCheck] == 0.0) {
                minItem.coeffsArray.removeAt(indexCheck)
                indexCheck++
            } else break
        }
        for (i in 0 until minItem.coeffsArray.size) {
            for (j in 0 until maxItem.coeffsArray.size) {
                while (answer.coeffsArray.size != maxPower + minItem.coeffsArray.size) {
                    answer.coeffsArray.add(0.0)
                }
                answer.coeffsArray[i + j] += maxItem.coeffsArray[j] * minItem.coeffsArray[i]
            }
        }
        return answer
    }

    /**
     * Деление
     *
     * Про операции деления и взятия остатка см. статью Википедии
     * "Деление многочленов столбиком". Основные свойства:
     *
     * Если A / B = C и A % B = D, то A = B * C + D и степень D меньше степени B
     */
    operator fun div(other: Polynom): Polynom = this.divideBy(other).first

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom {
        val remainder = this.divideBy(other).second
        while (remainder.coeffsArray[0] == 0.0 && remainder.coeffsArray.size != 1) {
            remainder.coeffsArray.removeAt(0)
        }
        return remainder
    }

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Polynom) return false
        return this.getValue(1.0) == other.getValue(1.0)
    }

    /**
     * Получение хеш-кода
     */
    override fun hashCode(): Int = this.coeffsArray.hashCode()

    fun divideBy(other: Polynom, remainderTrigger: Boolean = false): Pair<Polynom, Polynom> {
        var dividend = Polynom()
        dividend.coeffsArray.addAll(this.coeffsArray)
        val divisor = Polynom()
        divisor.coeffsArray.addAll(other.coeffsArray)
        val answer = Polynom()
        var tmp: Polynom
        var indexForAnswer = 0
        val specialAnswerPolynom = Polynom(0.0)
        while (divisor.coeffsArray[0] == 0.0) {
            divisor.coeffsArray.removeAt(0)
        }
        val sizeOfAnswerList = this.degree() - divisor.degree() + 1
        while (answer.coeffsArray.size != sizeOfAnswerList) {
            if (dividend.coeffsArray[0] == 0.0) {
                dividend.coeffsArray.removeAt(0)
            }
            answer.coeffsArray.add(dividend.coeffsArray[0] / divisor.coeffsArray[0])
            specialAnswerPolynom.coeffsArray[0] =
                dividend.coeffsArray[0] / divisor.coeffsArray[0]
            tmp = specialAnswerPolynom * divisor
            while (tmp.degree() != dividend.degree()) {
                tmp.coeffsArray.add(0.0)
            }
            dividend -= tmp
            indexForAnswer++
        }
        return Pair(answer, dividend)
    }

    fun flattener(other: Polynom): Triple<Polynom, Polynom, Polynom> {
        val first = Polynom()
        first.coeffsArray.addAll(this.coeffsArray)
        val second = Polynom()
        second.coeffsArray.addAll(other.coeffsArray)
        val answer = Polynom()
        var index = 0
        while (first.coeffsArray.size != second.coeffsArray.size) {
            if (minOf(first.coeffsArray.size, second.coeffsArray.size) == first.coeffsArray.size) {
                first.coeffsArray.add(index, 0.0)
            } else if (minOf(first.coeffsArray.size, second.coeffsArray.size) == second.coeffsArray.size) {
                second.coeffsArray.add(index, 0.0)
            }
            index++
        }
        answer.coeffsArray.addAll(first.coeffsArray)
        return Triple(first, second, answer)
    }
}
