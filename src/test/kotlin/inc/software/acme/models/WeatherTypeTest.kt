package inc.software.acme.models

import inc.software.acme.models.WeatherType.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class WeatherTypeTest {

    @Test
    fun combine() {
        assertEquals(Rainy, Sunny.combine(Rainy))
        assertEquals(Rainy, Rainy.combine(Sunny))
        assertEquals(Rainy, Rainy.combine(Rainy))
        assertEquals(Rainy, Other.combine(Rainy))
        assertEquals(Rainy, Rainy.combine(Other))
    }
}