package pinboard.format

import org.apache.commons.logging.LogFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
object FormatUtils {

	private val log = LogFactory.getLog(javaClass)

	private val decodeDateFormat = utcAwareDateFormatter("yyyy-MM-dd'T'HH:mm:ss'Z'")
	private val encodeDateFormat = utcAwareDateFormatter("yyyy-MM-dd'T'HH:mm:ss'Z'")
	private val decodeNoteDateFormat = utcAwareDateFormatter("yyyy-MM-dd HH:mm:ss")
	private val encodeNoteDateFormat = utcAwareDateFormatter("yyyy-MM-dd HH:mm:ss")

	fun encodeNoteDate(d: Date): String = encodeNoteDateFormat.format(d)

	fun decodeNoteDate(noteDate: String): Date =
			decodeNoteDateFormat.parse(noteDate).apply {
				log.debug("decodeNoteDate: $noteDate")
			}

	fun encodeDate(d: Date): String = encodeDateFormat.format(d)

	fun decodeDate(date: String): Date =
			decodeDateFormat.parse(date).apply {
				log.debug("decode date: $date")
			}

	fun encodeYesOrNo(b: Boolean) = if (b) "yes" else "no"

	fun decodeYesOrNo(yesOrNoStr: String) = yesOrNoStr.toLowerCase().trim().contentEquals("yes")

	private fun utcAwareDateFormatter(sdfString: String) =
			SimpleDateFormat(sdfString).apply {
				timeZone = TimeZone.getTimeZone("UTC")
			}

}
