package inc.software.acme.services

import com.squareup.moshi.Moshi
import inc.software.acme.models.WeatherInfo
import inc.software.acme.models.WeatherResponseJsonAdapter
import inc.software.acme.models.toWeatherInfo
import okhttp3.OkHttpClient
import okhttp3.Request

private val API_KEY = System.getenv("API_KEY")

class OpenWeatherService constructor(private val httpClient: OkHttpClient, private val moshi: Moshi) {

    // ideally this would return a Result<List<WeatherInfo>> *and be checked for use by the compiler* like in Rust.
    // unfortunately that doesn't exist yet in Kotlin, but looks like it is on the horizon
    // https://github.com/Kotlin/KEEP/blob/master/proposals/stdlib/result.md#future-advancements
    // this would be a good place to discuss with the team how we want to handle failible APIs
    // and our general thoughts about exception handling
    fun requestForecast(): List<WeatherInfo> {
        val req = Request.Builder()
                .url("http://api.openweathermap.org/data/2.5/forecast?q=minneapolis,us&units=imperial&APPID=$API_KEY")
                .get()
                .build()

        httpClient.newCall(req).execute().use { response ->
            return if (!response.isSuccessful) {
                throw Exception("Failed to load data from Open Weather API. Status code=${response.code}")
            } else {
                response.body.use { body ->
                    if (body == null) {
                        throw Exception("API Response did not contain any data")
                    }

                    val deserialized = WeatherResponseJsonAdapter(moshi).fromJson(body.source())
                            ?: throw Exception("Failed to deserialize response body. $response")

                    deserialized.toWeatherInfo()
                }
            }
        }
    }
}