package inc.software.acme.services

import inc.software.acme.models.ContactMethod.*
import inc.software.acme.models.WeatherInfo
import inc.software.acme.models.WeatherType.*
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class ContactServiceTest {
    @MockK
    lateinit var openWeatherService: OpenWeatherService

    @Test
    fun `test getContactMethod`() {
        assertEquals(Text, getContactMethod(DailyWeather(averageTemp = 75.1F, type = Sunny, count = 1)))
        assertEquals(Email, getContactMethod(DailyWeather(averageTemp = 75F, type = Sunny, count = 1)))
        assertEquals(Email, getContactMethod(DailyWeather(averageTemp = 75F, type = Other, count = 1)))
        assertEquals(Email, getContactMethod(DailyWeather(averageTemp = 55F, type = Other, count = 1)))
        assertEquals(Phone, getContactMethod(DailyWeather(averageTemp = 54.9F, type = Sunny, count = 1)))
        assertEquals(Phone, getContactMethod(DailyWeather(averageTemp = 90.0F, type = Rainy, count = 1)))
    }


    private val testData = listOf(
            Text to listOf(
                    WeatherInfo(
                            time = Instant.now() - Duration.ofHours(3),
                            temp = 90F,
                            weatherType = Sunny),
                    WeatherInfo(
                            time = Instant.now(),
                            temp = 90F,
                            weatherType = Sunny),
                    WeatherInfo(
                            time = Instant.now() + Duration.ofHours(3),
                            temp = 90F,
                            weatherType = Sunny)),

            Phone to listOf(
                    WeatherInfo(
                            time = Instant.now() - Duration.ofHours(3),
                            temp = 90F,
                            weatherType = Sunny),
                    WeatherInfo(
                            time = Instant.now(),
                            temp = 90F,
                            weatherType = Sunny),
                    WeatherInfo(
                            time = Instant.now() + Duration.ofHours(3),
                            temp = 90F,
                            weatherType = Rainy)),

            Email to listOf(
                    WeatherInfo(
                            time = Instant.now() - Duration.ofHours(3),
                            temp = 55F,
                            weatherType = Other),
                    WeatherInfo(
                            time = Instant.now(),
                            temp = 65F,
                            weatherType = Other),
                    WeatherInfo(
                            time = Instant.now() + Duration.ofHours(3),
                            temp = 70F,
                            weatherType = Other))
    )

    @TestFactory
    fun `test determineContactMethod`() = testData.map { (expected, input) ->
        // this name might mess up the output
        DynamicTest.dynamicTest("should get $expected when determining contact method from $input")
        {
            every { openWeatherService.requestForecast() } returns input
            val serviceUnderTest = ContactService(openWeatherService)

            val result = serviceUnderTest.determineContactMethod()

            val now = LocalDate.now()
            assert(result.containsKey(now))
            val method = result[now]
            assertNotNull(method)
            assertEquals(expected, method)
        }
    }
}