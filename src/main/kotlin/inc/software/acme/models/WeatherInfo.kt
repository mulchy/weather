package inc.software.acme.models

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.time.Instant

data class WeatherInfo(val time: Instant, val temp: Float, val weatherType: WeatherType)

enum class WeatherType {
    Sunny,
    Rainy,
    Other;

    /**
     * Combines two WeatherTypes into a single WeatherType. If either value is [Rainy], the result will be [Rainy].
     * If both values are [Sunny], the result will be [Sunny]. Otherwise, the result will be [Other]
     */
    fun combine(other: WeatherType): WeatherType {
        return when {
            this == Rainy || other == Rainy -> {
                Rainy
            }
            this == Sunny && other == Sunny -> {
                Sunny
            }
            else -> {
                Other
            }
        }
    }

    companion object {
        /**
         * [https://openweathermap.org/weather-conditions](https://openweathermap.org/weather-conditions)
         */
        fun fromNumber(input: Number): WeatherType {
            return when (input) {
                in 200..300 -> {
                    Rainy
                }
                in 300..400 -> {
                    Rainy
                }
                in 500..600 -> {
                    Rainy
                }

                in 800..900 -> {
                    Sunny
                }

                else -> {
                    Other
                }
            }
        }
    }
}

class WeatherTypeAdapter {
    @FromJson
    fun fromJson(jsonReader: JsonReader): WeatherType {
        return WeatherType.fromNumber(jsonReader.nextInt())
    }

    @ToJson
    fun toJson(@SuppressWarnings("unused") jsonWriter: JsonWriter, @SuppressWarnings("unused") instant: Instant) {
        throw Exception("not implemented")
    }
}