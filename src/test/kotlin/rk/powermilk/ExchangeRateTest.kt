package rk.powermilk

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.FileNotFoundException
import java.net.MalformedURLException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ExchangeRateTest {
    private val exchangeRate: ExchangeRate = ExchangeRate("src/test/kotlin/resources/exchangeRate.json")

    @Test
    fun `should throws MalformedURLException`() {
        val badUrl = "htp://api.nbp.pl/api/exchangerates/rates/a/gbp/last/10/?format=json"
        val exception = assertThrows<MalformedURLException> { ExchangeRate(badUrl) }

        assertEquals(
            "$badUrl isn't valid URL",
            exception.message
        )
    }

    @Test
    fun `should throws FileNotFoundException`() {
        val exception = assertThrows<FileNotFoundException> {
            ExchangeRate("NonExist.json")
        }

        assertEquals("File NonExist.json not found", exception.message)
    }

    @ParameterizedTest
    @MethodSource("messageProvider")
    fun `should throw IllegalStateException when rates are empty`(isMax: Boolean, expectedMessage: String) {
        val exception = assertThrows<IllegalStateException> {
            ExchangeRate(testPath("missingRates.json")).apply {
                if (isMax) getMax() else getMin()
            }
        }

        assertEquals(expectedMessage, exception.message)
    }

    @Test
    fun `should return maximum value`() = assertEquals(5.1857, exchangeRate.getMax())

    @Test
    fun `should return minimum value`() = assertEquals(5.0371, exchangeRate.getMin())

    @Test
    fun `should throw exception for empty rates list`() {
        val exception = assertThrows<IllegalStateException> {
            ExchangeRate(testPath("emptyRates.json")).getMin()
        }
        assertEquals("The minimum value couldn't be obtained", exception.message)
    }

    @Test
    fun `should throw exception for malformed JSON`() {
        val exception = assertThrows<JsonMappingException> {
            ExchangeRate(testPath("malformed.json")).getMin()
        }
        assertTrue { exception.message?.startsWith("Cannot deserialize value of type") ?: false }
    }

    @Test
    fun `should throw IllegalStateException for empty rates`() {
        val exception = assertThrows<IllegalStateException> {
            ExchangeRate(testPath("emptyRates.json")).getMin()
        }
        assertEquals("The minimum value couldn't be obtained", exception.message)
    }

    @Test
    fun `should throw RuntimeException for malformed json`() {
        assertThrows<InvalidFormatException> {
            ExchangeRate(testPath("malformed.json")).getMin()
        }
    }

    @Test
    fun `should throw IllegalStateException for missing mid`() {
        val exception = assertThrows<IllegalStateException> {
            ExchangeRate(testPath("missingMid.json")).getMin()
        }
        assertEquals("The minimum value couldn't be obtained", exception.message)
    }
    @Test
    fun `should throw IllegalStateException for missing rates`() {
        val exception = assertThrows<IllegalStateException> {
            ExchangeRate(testPath("missingRates.json")).getMax()
        }
        assertEquals("The maximum value couldn't be obtained", exception.message)
    }

    @Test
    fun `should return correct value for single rate`() {
        val exchange = ExchangeRate(testPath("singleRate.json"))
        assertEquals(4.5, exchange.getMin())
        assertEquals(4.5, exchange.getMax())
    }

    @Test
    fun `should throw IllegalStateException for wrong key 'ratez'`() {
        val exception = assertThrows<IllegalStateException> {
            ExchangeRate(testPath("invalidKey.json")).getMin()
        }
        assertEquals("The minimum value couldn't be obtained", exception.message)
    }

    @Test
    fun `should throw FileNotFoundException for non-existent file`() {
        val exception = assertThrows<FileNotFoundException> {
            ExchangeRate(testPath("notExist.json"))
        }
        assertEquals("File ${testPath("notExist.json")} not found", exception.message)
    }

    private fun testPath(name: String) =
        "src/test/kotlin/resources/$name"

    private fun messageProvider() = listOf(
        Arguments.of(true, "The maximum value couldn't be obtained"),
        Arguments.of(false, "The minimum value couldn't be obtained")
    )
}
