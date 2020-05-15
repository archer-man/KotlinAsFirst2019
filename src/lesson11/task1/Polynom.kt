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
            result = result + x.pow(coeffPower) * coefficient
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
        for (coefficient in coeffsArray) {
            if (coefficient != 0.0) return (coeffsArray.size - coeffsArray.indexOf(coefficient) - 1) else continue
        }
        return 0
    }

    /**
     * Сложение
     */
    operator fun plus(other: Polynom): Polynom {
        val list = mutableListOf<Double>()
        var index = -1
        var changeOfObjectIndex = -1
        val maxItem: Polynom
        val minItem: Polynom
        if (minOf(this.coeffsArray.size, other.coeffsArray.size) == this.coeffsArray.size) {
            list.addAll(this.coeffsArray)
            maxItem = other
            minItem = this
        } else {
            maxItem = this
            minItem = other
            list.addAll(other.coeffsArray)
        }
        while (list.size != maxItem.coeffsArray.size) {
            index++
            changeOfObjectIndex++
            list.add(index, 0.0)
            minItem.coeffsArray.add(index, 0.0)
        }
        for (i in 0 until list.size) {
            list[i] = this.coeffsArray[i] + other.coeffsArray[i]
        }
        val instance = Polynom()
        instance.coeffsArray.addAll(list)
        while (changeOfObjectIndex != -1) {
            minItem.coeffsArray.removeAt(changeOfObjectIndex)
            changeOfObjectIndex -= 1
        }
        return instance
    }

    /**
     * Смена знака (при всех слагаемых)
     */
    operator fun unaryMinus(): Polynom {
        var index = -1
        for (coefficient in this.coeffsArray) {
            index++
            this.coeffsArray[index] = coefficient * -1
        }
        return this
    }

    /**
     * Вычитание
     */
    operator fun minus(other: Polynom): Polynom {
        val minuend = Polynom()
        minuend.coeffsArray.addAll(this.coeffsArray)
        val subtrahend = Polynom()
        subtrahend.coeffsArray.addAll(other.coeffsArray)
        val answer = Polynom()
        var index = 0
        var changeWasTaken = 0
        while (minuend.coeffsArray.size != subtrahend.coeffsArray.size) {
            if (minOf(minuend.coeffsArray.size, subtrahend.coeffsArray.size) == minuend.coeffsArray.size) {
                minuend.coeffsArray.add(index, 0.0)
                changeWasTaken++
            } else if (minOf(minuend.coeffsArray.size, subtrahend.coeffsArray.size) == subtrahend.coeffsArray.size) {
                subtrahend.coeffsArray.add(index, 0.0)
            }
            index++
            changeWasTaken++
        }
        answer.coeffsArray.addAll(minuend.coeffsArray)
        for (i in 0 until maxOf(minuend.coeffsArray.size, subtrahend.coeffsArray.size)) {
            answer.coeffsArray[i] = minuend.coeffsArray[i] - subtrahend.coeffsArray[i]
        }
        return answer
    }

    /**
     * Умножение
     */
    operator fun times(other: Polynom): Polynom {
        val maxItem: Polynom
        val minItem: Polynom
        var changeOfObjectIndex = -1
        val list = mutableListOf<Double>()
        val maxPower = maxOf(this.coeffsArray.size, other.coeffsArray.size) - 1
        var indexCheck = 0

        if (minOf(this.coeffsArray.size, other.coeffsArray.size) == this.coeffsArray.size) {
            maxItem = other
            minItem = this
            list.addAll(maxItem.coeffsArray)
            list.fill(0.0)
        } else {
            maxItem = this
            minItem = other
            list.addAll(maxItem.coeffsArray)
            list.fill(0.0)
        }
        while (indexCheck < minItem.coeffsArray.size) {
            if (minItem.coeffsArray[indexCheck] == 0.0) {
                minItem.coeffsArray.removeAt(indexCheck)
                indexCheck++
            } else break
        }
        for (i in 0 until minItem.coeffsArray.size) {
            for (j in 0 until maxItem.coeffsArray.size) {
                while (list.size != maxPower + minItem.coeffsArray.size) {
                    list.add(0.0)
                }
                list[i + j] = list[i + j] + maxItem.coeffsArray[j] * minItem.coeffsArray[i]
            }
        }
        val instance = Polynom()
        instance.coeffsArray.addAll(list)
        while (changeOfObjectIndex != -1) {
            minItem.coeffsArray.removeAt(changeOfObjectIndex)
            changeOfObjectIndex -= 1
        }
        return instance
    }

    /**
     * Деление
     *
     * Про операции деления и взятия остатка см. статью Википедии
     * "Деление многочленов столбиком". Основные свойства:
     *
     * Если A / B = C и A % B = D, то A = B * C + D и степень D меньше степени B
     */
    operator fun div(other: Polynom): Polynom {
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
        val sizeOfAnswerList = this.degree() - other.degree() + 1
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
        return answer
    }

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom {
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
        val sizeOfAnswerList = this.coeffsArray.size - divisor.coeffsArray.size + 1
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
        while (dividend.coeffsArray[0] == 0.0 && dividend.coeffsArray.size != 1) {
            dividend.coeffsArray.removeAt(0)
        }
        return dividend
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
}
