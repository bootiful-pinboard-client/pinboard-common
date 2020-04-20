package pinboard.format.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import pinboard.format.FormatUtils
import java.util.*

/**
 * a serializer for the specific variant of dates encountered
 * when parsing Pinboard `Note` instance `created_at` and
 * `updated_at` fields.
 *
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
class NoteTimeSerializer(t: Class<Date>?) : StdSerializer<Date>(t) {

	private constructor() : this(Date::class.java)

	override fun serialize(value: Date?,
	                       gen: JsonGenerator?,
	                       provider: SerializerProvider?) {
		if (value != null)
			gen!!.writeString(FormatUtils.encodeNoteDate(value!!))
	}
}