package pinboard.format.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.BDDAssertions
import org.junit.jupiter.api.Test
import pinboard.Note
import pinboard.Notes
import pinboard.format.FormatUtils
import java.util.*

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
class NoteTimeSerializationTest {

    private val dateTime = """  2010-02-11 19:18:58  """.trim()
    private val objectMapper = ObjectMapper()
    private val date: Date by lazy {
        val gc = GregorianCalendar()
        gc.set(Calendar.YEAR, 2010)
        gc.set(Calendar.MONTH, Calendar.FEBRUARY)
        gc.set(Calendar.DAY_OF_MONTH, 11)
        gc.set(Calendar.HOUR_OF_DAY, 19)
        gc.set(Calendar.MINUTE, 18)
        gc.set(Calendar.SECOND, 58)
        gc.timeZone = TimeZone.getTimeZone("UTC")
        gc.time
    }

    @Test
    fun serializeDatesWithFormatUtils() {
        val asString = FormatUtils.encodeNoteDate(date)
        BDDAssertions.then(asString).contains(dateTime)
    }

    @Test
    fun serializeDatesWithJackson() {
        val bookmark = Note("id", "title", 2, date, date, "hash")
        val asString = objectMapper.writeValueAsString(bookmark)
        BDDAssertions.then(asString).contains(dateTime)
    }


    @Test
    fun deserializeDatesWithFormatUtils() {
        val dateString = """ 2017-08-05 02:10:02  """.trim()

        val noteDate = FormatUtils.decodeNoteDate(dateString)

        val cal = GregorianCalendar()
        cal.time = noteDate
        cal.timeZone = TimeZone.getTimeZone("UTC")

        BDDAssertions.then(cal.get(Calendar.YEAR)).isEqualTo(2017)
        BDDAssertions.then(cal.get(Calendar.MONTH)).isEqualTo(Calendar.AUGUST)
        BDDAssertions.then(cal.get(Calendar.DAY_OF_MONTH)).isEqualTo(5)
        BDDAssertions.then(cal.get(Calendar.HOUR_OF_DAY)).isEqualTo(2)
        BDDAssertions.then(cal.get(Calendar.MINUTE)).isEqualTo(10)
        BDDAssertions.then(cal.get(Calendar.SECOND)).isEqualTo(2)
    }

    @Test
    fun deserializeDatesWithJackson() {

        val serializedNotes = """
                {
                    "count": 1,
                    "notes": [
                        {"id":"d978560ccfb4dee9ee89","hash":"4c948d413b969450a941","title":"another note","length":"20", "created_at":"2017-08-05 02:10:02" }
                    ]
                }
        """

        val notes = this.objectMapper.readValue<Notes>(serializedNotes, Notes::class.java)

        val cal = GregorianCalendar()
        cal.time = notes.notes!![0].created
        cal.timeZone = TimeZone.getTimeZone("UTC")

        BDDAssertions.then(cal.get(Calendar.YEAR)).isEqualTo(2017)
        BDDAssertions.then(cal.get(Calendar.MONTH)).isEqualTo(Calendar.AUGUST)
        BDDAssertions.then(cal.get(Calendar.DAY_OF_MONTH)).isEqualTo(5)
        BDDAssertions.then(cal.get(Calendar.HOUR_OF_DAY)).isEqualTo(2)
        BDDAssertions.then(cal.get(Calendar.MINUTE)).isEqualTo(10)
        BDDAssertions.then(cal.get(Calendar.SECOND)).isEqualTo(2)
    }
}