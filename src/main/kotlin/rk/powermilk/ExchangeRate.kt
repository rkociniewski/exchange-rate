package rk.powermilk

import com.fasterxml.jackson.annotation.JsonProperty
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.kotlinModule
import tools.jackson.module.kotlin.readValue
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URI
import kotlin.io.path.Path
import kotlin.io.path.exists

/**
 * Class with calculated exchange rates from proper URL or file.
 *
 * @param urlOrFilePath URL or file path to JSON with exchange in NBP (National Bank of Poland) format.
 */
class ExchangeRate(urlOrFilePath: String = "http://api.nbp.pl/api/exchangerates/rates/a/gbp/last/10/?format=json") {

    /**
     * Jackson ObjectMapper to serialize and deserialize data.
     */
    private val mapper = JsonMapper.builder()
        .addModule(kotlinModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .build()

    /**
     * ExchangeData object to get proper value of exchange rates.
     */
    private val exchangeData = urlOrFilePath.createExchangeData()

    fun getMin() =
        exchangeData.rates.mapNotNull { it.mid }.minOrNull()
            ?: error("The minimum value couldn't be obtained")

    fun getMax() =
        exchangeData.rates.mapNotNull { it.mid }.maxOrNull()
            ?: error("The maximum value couldn't be obtained")

    private fun String.createExchangeData(): ExchangeData {
        val urlRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]".toRegex()
        val reader = when {
            urlRegex.matches(this) -> InputStreamReader(URI(this).toURL().openStream())
            this.contains(':') -> throw MalformedURLException("$this isn't valid URL")
            Path(this).exists() -> FileReader(File(this))
            else -> throw FileNotFoundException("File $this not found")
        }

        return reader.use { mapper.readValue(it) }
    }

    /**
     * Class representing exchange data.
     */
    data class ExchangeData(
        @param:JsonProperty("rates")
        val rates: List<Rate> = listOf()
    )

    /**
     * Class with average rate from day.
     */
    data class Rate(
        @param:JsonProperty("mid")
        val mid: Double?
    )
}
