package org.powermilk

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.annotations.Expose
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import kotlin.io.path.Path
import kotlin.io.path.exists

/**
 * Class with calculated exchange rates from proper URL or file.
 *
 * @param urlOrFilePath URL or file path to JSON with exchange in NBP (National Bank of Poland) format.
 */
class ExchangeRate(urlOrFilePath: String = "http://api.nbp.pl/api/exchangerates/rates/a/gbp/last/10/?format=json") {

    /**
     * [Gson] to serialize and deserialize data.
     */
    private val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create()

    /**
     * [ExchangeData] object to get proper value of exchange rates.
     */
    private val exchangeData = urlOrFilePath.createExchangeData()

    /**
     * Function get minimum value of exchange rate.
     *
     * @return minimum exchange rate.
     * @throws JsonParseException throws when there isn't minimum value.
     */
    @Throws(JsonParseException::class)
    fun getMin() =
        exchangeData.rates.minOfOrNull { it.mid } ?: throw JsonParseException("The minimum value couldn't be obtained")

    /**
     * Function get maximum value of exchange rate.
     *
     * @return maximum exchange rate.
     * @throws JsonParseException throws when there isn't maximum value.
     */
    @Throws(Exception::class)
    fun getMax() =
        exchangeData.rates.maxOfOrNull { it.mid } ?: throw JsonParseException("The maximum value couldn't be obtained")

    /**
     * Function extension creates [ExchangeRate] from [String].
     *
     * @exception MalformedURLException when [String] isn't valid URL.
     * @exception FileNotFoundException when [String] isn't valid URL and file path.
     */
    @Throws(MalformedURLException::class, FileNotFoundException::class)
    private fun String.createExchangeData(): ExchangeData {
        val urlRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]".toRegex()
        val reader = when {
            urlRegex.matches(this) -> InputStreamReader(URL(this).openStream())
            this.contains(':') -> throw MalformedURLException("$this isn't valid URL")
            Path(this).exists() -> FileReader(File(this))
            else -> throw FileNotFoundException("File $this not found")
        }

        return gson.fromJson(reader, ExchangeData::class.java)
    }

    /**
     * Class representing exchange data.
     *
     * @param rates [Rate] list.
     */
    private data class ExchangeData(
        /**
         * rates list
         */
        @Expose val rates: List<Rate> = listOf()
    )

    /**
     * Class with avarage rate from day.
     *
     * @param mid avarage rate from day.
     */
    private data class Rate(
        /**
         * avarage rate from day.
         */
        @Expose val mid: Double = 0.0
    )
}
