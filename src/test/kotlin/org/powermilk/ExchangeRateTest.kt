package org.powermilk

import com.google.gson.JsonParseException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.FileNotFoundException
import java.net.MalformedURLException
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ExchangeRateTest {
    private val exchangeRate: ExchangeRate = ExchangeRate("src/test/kotlin/resources/exchangeRate.json")

    @Test
    fun `should throws MalformedURLException`() {
        val badUrl = "htp://api.nbp.pl/api/exchangerates/rates/a/gbp/last/10/?format=json"
        assertEquals(
            assertThrows<MalformedURLException> { ExchangeRate(badUrl) }.message,
            "htp://api.nbp.pl/api/exchangerates/rates/a/gbp/last/10/?format=json isn't valid URL"
        )
    }

    @Test
    fun `should throws FileNotFoundException`() {
        val exception = assertThrows<FileNotFoundException> {
            ExchangeRate("NonExist.json")
        }

        assertEquals(exception.message, "File NonExist.json not found")
    }

    @ParameterizedTest
    @MethodSource("messageProvider")
    fun `should throws JsonParseException`(isMax: Boolean, expectedMessage: String) {
        val exception = assertThrows<JsonParseException> {
            ExchangeRate("src/test/kotlin/resources/badRate.json").apply {
                if (isMax) getMax()
                else getMin()
            }
        }

        assertEquals(expectedMessage, exception.message)
    }

    @Test
    fun `should return maximum value`() = assertEquals(5.1857, exchangeRate.getMax())

    @Test
    fun `should return minimum value`() = assertEquals(5.0371, exchangeRate.getMin())

    private fun messageProvider() = listOf(
        Arguments.of(true, "The maximum value couldn't be obtained"),
        Arguments.of(false, "The minimum value couldn't be obtained")
    )
}
