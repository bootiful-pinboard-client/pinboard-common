package pinboard.format.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.BDDAssertions
import org.junit.jupiter.api.Test
import pinboard.Bookmark
import java.util.*

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
class TimeSerializationTest {

    private val dateTime = """  2017-08-01T19:18:58Z  """.trim()
    private val objectMapper = ObjectMapper()
    private val date: Date by lazy {
        val gc = GregorianCalendar()
        gc.set(Calendar.YEAR, 2017)
        gc.set(Calendar.MONTH, Calendar.AUGUST)
        gc.set(Calendar.DAY_OF_MONTH, 1)
        gc.set(Calendar.HOUR_OF_DAY, 19)
        gc.set(Calendar.MINUTE, 18)
        gc.set(Calendar.SECOND, 58)
        gc.timeZone = TimeZone.getTimeZone("UTC")
        gc.time
    }

    @Test
    fun serializeDates() {
        val bookmark = Bookmark(("http://abc.com/a/b/c"), "description", "extended description", "hash",
                "meta", date, false, true, arrayOf("a", "b"))
        val asString = objectMapper.writeValueAsString(bookmark)
        BDDAssertions.then(asString).contains(dateTime)
    }

    @Test
    fun deserializeDates() {

        val serializedBookmark = """
        {
            "href":"https://spring.io/blog/2017/07/26/spring-boot-1-5-5-available-now",
            "description":"Spring Boot 1.5.5 available now",
            "extended":"Spring Boot  ",
            "meta":"42574efde586ee9671132da090d704cc",
            "hash":"abadeea6b8fb3df8879edc74c25afa42",
            "time":"$dateTime",
            "shared":"no",
            "toread":"yes"
        }
        """

        val bookmark = this.objectMapper.readValue<Bookmark>(serializedBookmark, Bookmark::class.java)

        val cal = GregorianCalendar()
        cal.time = bookmark.time
        cal.timeZone = TimeZone.getTimeZone("UTC")
        BDDAssertions.then(cal.get(Calendar.YEAR)).isEqualTo(2017)
        BDDAssertions.then(cal.get(Calendar.MONTH)).isEqualTo(Calendar.AUGUST)
        BDDAssertions.then(cal.get(Calendar.DAY_OF_MONTH)).isEqualTo(1)
        BDDAssertions.then(cal.get(Calendar.MINUTE)).isEqualTo(18)
        BDDAssertions.then(cal.get(Calendar.HOUR_OF_DAY)).isEqualTo(19)
        BDDAssertions.then(cal.get(Calendar.SECOND)).isEqualTo(58)
    }


}