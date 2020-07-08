package inc.software.acme.models

import com.squareup.moshi.*
import java.time.Instant

/**
 * This class is intended to be a close mapping of the JSON response returned by the Open Weather API.
 * It is used only for deserialization and shouldn't be used directly by application code.
 * Instead, it should be converted list of [WeatherInfo] using [toWeatherInfo]
 */
@JsonClass(generateAdapter = true)
data class WeatherResponse(val cnt: Int, val list: List<Forecast>) {
    @JsonClass(generateAdapter = true)
    data class Forecast(@Json(name="dt") val time: Instant, val main: Main, val weather: List<Weather>) {
        @JsonClass(generateAdapter = true)
        data class Main(val temp: Float)
        @JsonClass(generateAdapter = true)
        data class Weather(val id: WeatherType)
    }
}

/**
 * Converts a deserialized [WeatherResponse] into a list of [WeatherInfo]. This function aggregates multiple weather
 * conditions for a given instant into a single "primary" weather conditions. For details on how conditions are combined,
 * @see [WeatherType.combine]
 */
fun WeatherResponse.toWeatherInfo(): List<WeatherInfo> {
    val accumulator: (WeatherType, WeatherResponse.Forecast.Weather) -> WeatherType = {type, weather -> type.combine(weather.id) }
    return this.list.map { f ->
        WeatherInfo(f.time, f.main.temp, f.weather.fold(WeatherType.Sunny, accumulator))
    }
}

// these should maybe live in their own file
val toInstant: (Long) -> Instant = {
    Instant.ofEpochSecond(it)
}
class InstantAdapter {
    @FromJson fun fromJson(jsonReader: JsonReader): Instant {
        return toInstant(jsonReader.nextLong())
    }

    @ToJson fun toJson(jsonWriter: JsonWriter, instant: Instant) {
        jsonWriter.value(instant.epochSecond)
    }
}

