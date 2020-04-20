package pinboard.format.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import pinboard.format.FormatUtils
import java.util.*

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
class TimeSerializer(t: Class<Date>?) : StdSerializer<Date>(t) {

	private constructor() : this(Date::class.java)

	override fun serialize(value: Date?,
	                       gen: JsonGenerator?,
	                       provider: SerializerProvider?) {
		if (null != value)
			gen!!.writeString(FormatUtils.encodeDate(value))
	}
}