@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

import kotlinx.html.InputType
import ru.spbstu.kotlin.typeclass.kind

/**
 * Класс "расписание поездов".
 *
 * Общая сложность задания -- средняя.
 * Объект класса хранит расписание поездов для определённой станции отправления.
 * Для каждого поезда хранится конечная станция и список промежуточных.
 * Поддерживаемые методы:
 * добавить новый поезд, удалить поезд,
 * добавить / удалить промежуточную станцию существующему поезду,
 * поиск поездов по времени.
 *
 * В конструктор передаётся название станции отправления для данного расписания.
 */
class TrainTimeTable(val baseStationName: String) {
    /**
     * Добавить новый поезд.
     *
     * Если поезд с таким именем уже есть, следует вернуть false и ничего не изменять в таблице
     *
     * @param train название поезда
     * @param depart время отправления с baseStationName
     * @param destination конечная станция
     * @return true, если поезд успешно добавлен, false, если такой поезд уже есть
     */

    var trainTable = mutableListOf<Train>()

    fun addTrain(train: String, depart: Time, destination: Stop): Boolean {
        if (trainTable.isNotEmpty()) {
            val namesOfTrains = mutableListOf<String>()
            for ((name) in trainTable) {
                namesOfTrains.add(name)
            }
            if (!namesOfTrains.contains(train)) {
                val customList = mutableListOf<Stop>()
                customList.add(Stop(baseStationName, depart))
                customList += destination
                val element = Train(train, customList)
                trainTable.add(element)
                return true
            }

        }
        if (trainTable.isEmpty()) {
            val customList = mutableListOf<Stop>()
            customList.add(Stop(baseStationName, depart))
            customList += destination
            val element = Train(train, customList)
            trainTable.add(element)
            return true
        }
        return false
    }

    /**
     * Удалить существующий поезд.
     *
     * Если поезда с таким именем нет, следует вернуть false и ничего не изменять в таблице
     *
     * @param train название поезда
     * @return true, если поезд успешно удалён, false, если такой поезд не существует
     */
    fun removeTrain(train: String): Boolean {
        for ((name) in trainTable) {
            if (name == train) {
                trainTable.removeAll { it.name == train }
                return true
            }
        }
        return false
    }


    /**
     * Добавить/изменить начальную, промежуточную или конечную остановку поезду.
     *
     * Если у поезда ещё нет остановки с названием stop, добавить её и вернуть true.
     * Если stop.name совпадает с baseStationName, изменить время отправления с этой станции и вернуть false.
     * Если stop совпадает с destination данного поезда, изменить время прибытия на неё и вернуть false.
     * Если stop совпадает с одной из промежуточных остановок, изменить время прибытия на неё и вернуть false.
     *
     * Функция должна сохранять инвариант: время прибытия на любую из промежуточных станций
     * должно находиться в интервале между временем отправления с baseStation и временем прибытия в destination,
     * иначе следует бросить исключение IllegalArgumentException.
     * Также, время прибытия на любую из промежуточных станций не должно совпадать с временем прибытия на другую
     * станцию или с временем отправления с baseStation, иначе бросить то же исключение.
     *
     * @param train название поезда
     * @param stop начальная, промежуточная или конечная станция
     * @return true, если поезду была добавлена новая остановка, false, если было изменено время остановки на старой
     */
    fun addStop(train: String, stop: Stop): Boolean {
        var index = -1
        for (i in trainTable) {
            index += 1
            if (train == i.name) {
                val namesOfStops = mutableListOf<String>()
                for (stopItem in i.stops) {
                    namesOfStops.add(stopItem.name)
                    if (stop.time == stopItem.time && (stop.name != namesOfStops[0] && stop.name != namesOfStops[namesOfStops.lastIndex]) && stop.name != stopItem.name) throw IllegalArgumentException()
                }
                if (stop.name !in namesOfStops && (stop.time > i.stops[0].time && stop.time < i.stops[namesOfStops.lastIndex].time)) {
                    val tmp = i.stops.plus(stop).sortedBy { it.time }
                    trainTable[index] = Train(train, tmp)
                    return true
                }
                if (stop.name == namesOfStops[0] || stop.name == namesOfStops[namesOfStops.lastIndex] || stop.name in namesOfStops) {
                    val indexOfStop = namesOfStops.indexOf(stop.name)
                    val tmp = trainTable[index].stops.toMutableList()
                    tmp[indexOfStop] = stop
                    tmp.sortBy { it.time }
                    if (baseStationName != tmp[0].name || namesOfStops[namesOfStops.lastIndex] != tmp[tmp.lastIndex].name) throw IllegalArgumentException()
                    trainTable[index] = Train(train, tmp)
                    return false
                } else throw IllegalArgumentException()
            }
        }
        return false
    }

    /**
     * Удалить одну из промежуточных остановок.
     *
     * Если stopName совпадает с именем одной из промежуточных остановок, удалить её и вернуть true.
     * Если у поезда нет такой остановки, или stopName совпадает с начальной или конечной остановкой, вернуть false.
     *
     * @param train название поезда
     * @param stopName название промежуточной остановки
     * @return true, если удаление успешно
     */
    fun removeStop(train: String, stopName: String): Boolean {
        var index = -1
        for (i in trainTable) {
            index += 1
            if (train == i.name) {
                val namesOfStops = mutableListOf<String>()
                for (stopItem in i.stops) {
                    namesOfStops.add(stopItem.name)
                }
                return if (stopName in namesOfStops && stopName != namesOfStops[0] && stopName != namesOfStops[namesOfStops.lastIndex]) {
                    val indexOfStop = namesOfStops.indexOf(stopName)
                    val tmp = i.stops - i.stops[indexOfStop]
                    trainTable[index] = Train(train, tmp)
                    true
                } else false
            }
        }
        return false
    }

    /**
     * Вернуть список всех поездов, упорядоченный по времени отправления с baseStationName
     */
    fun trains(): List<Train> {
        trainTable.sortBy { it.stops.first().time }
        return trainTable
    }

    /**
     * Вернуть список всех поездов, отправляющихся не ранее currentTime
     * и имеющих остановку (начальную, промежуточную или конечную) на станции destinationName.
     * Список должен быть упорядочен по времени прибытия на станцию destinationName
     */
    fun trains(currentTime: Time, destinationName: String): List<Train> {
        val sortedTrainsList = mutableListOf<Train>()
        var index = -1
        for (train in trainTable) {
            index += 1
            val namesOfStops = mutableListOf<String>()
            for ((name) in train.stops) {
                namesOfStops.add(name)
            }
            val indexOfDestination = namesOfStops.indexOf(destinationName)
            if (currentTime <= train.stops[0].time && namesOfStops.contains(destinationName)) {
                sortedTrainsList.add(train)
            }
            sortedTrainsList.sortBy { it.stops[indexOfDestination].time }
        }
        return sortedTrainsList
    }

    /**
     * Сравнение на равенство.
     * Расписания считаются одинаковыми, если содержат одинаковый набор поездов,
     * и поезда с тем же именем останавливаются на одинаковых станциях в одинаковое время.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TrainTimeTable) return false
        for (train in this.trainTable) {
            if (other.trainTable.contains(train)) continue else return false
        }
        return true
    }

}

/**
 * Время (часы, минуты)
 */
data class Time(val hour: Int, val minute: Int) : Comparable<Time> {
    /**
     * Сравнение времён на больше/меньше (согласно контракту compareTo)
     */
    override fun compareTo(other: Time): Int {
        if (hour == other.hour && minute == other.minute) return 0
        if (hour < other.hour || (hour == other.hour && minute < other.minute)) return -1
        if (hour > other.hour || (hour == other.hour && minute > other.minute)) return 1
        return 0
    }
}

/**
 * Остановка (название, время прибытия)
 */
data class Stop(val name: String, var time: Time)

/**
 * Поезд (имя, список остановок, упорядоченный по времени).
 * Первой идёт начальная остановка, последней конечная.
 */
data class Train(val name: String, val stops: List<Stop>) {
    constructor(name: String, vararg stops: Stop) : this(name, stops.asList())
}