package inc.software.acme

import com.squareup.moshi.Moshi
import inc.software.acme.models.InstantAdapter
import inc.software.acme.models.WeatherTypeAdapter
import inc.software.acme.services.ContactService
import inc.software.acme.services.OpenWeatherService
import okhttp3.OkHttpClient

fun main() {
    val client = OkHttpClient()
    val moshi = Moshi
            .Builder()
            .add(InstantAdapter())
            .add(WeatherTypeAdapter())
            .build()

    // in a spring app these could be @Autowired and injected, but this is a small demo app
    val weatherService = OpenWeatherService(client, moshi)
    val contactService = ContactService(weatherService)

    val plan = contactService.determineContactMethod()

    plan.entries
            // since the Open Weather API gives us back data in UTC, its possible after we convert to our timezone we will have more than 5 days worth of info
            .take(5)
            .forEach { (day, method) ->
                println(day)
                println(method)
                println()
            }
}
