package inc.software.acme.services

import inc.software.acme.models.ContactMethod
import inc.software.acme.models.ContactMethod.*
import inc.software.acme.models.WeatherType
import inc.software.acme.models.WeatherType.Rainy
import inc.software.acme.models.WeatherType.Sunny
import java.time.LocalDate
import java.time.ZoneId

class ContactService(private val openWeatherService: OpenWeatherService) {

    /**
     * Aggregates the conditions for each day and determine the "overall weather".
     */
    fun determineContactMethod(): Map<LocalDate, ContactMethod?> {
        val forecast = openWeatherService.requestForecast()

        return forecast.groupingBy { info -> LocalDate.ofInstant(info.time, ZoneId.of("UTC-6")) }
                .aggregate { _, accumulator: DailyWeather?, element, first ->
                    if (first) {
                        DailyWeather(element.temp, 1, element.weatherType)
                    } else {
                        val newCount = accumulator!!.count + 1
                        val newAverage = accumulator.averageTemp + ((element.temp - accumulator.averageTemp) / newCount)
                        val newWeatherType = accumulator.type.combine(element.weatherType)

                        DailyWeather(newAverage, newCount, newWeatherType)
                    }
                }.mapValues { (_, dailyWeather) ->
                    dailyWeather?.let { getContactMethod(it) }
                }
    }
}


data class DailyWeather(val averageTemp: Float, val count: Int, val type: WeatherType)

fun getContactMethod(dailyWeather: DailyWeather): ContactMethod {
    return when {
        (dailyWeather.averageTemp > 75) && (dailyWeather.type == Sunny) -> {
            Text
        }
        (dailyWeather.averageTemp >= 55) && (dailyWeather.averageTemp <= 75) -> {
            Email
        }
        (dailyWeather.averageTemp < 55) || (dailyWeather.type == Rainy) -> {
            Phone
        }
        else -> {
            Wait
        }
    }
}