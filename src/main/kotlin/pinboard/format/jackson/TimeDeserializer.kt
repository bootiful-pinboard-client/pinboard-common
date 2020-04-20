package pinboard.format.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.apache.commons.logging.LogFactory
import org.springframework.util.StringUtils
import pinboard.format.FormatUtils
import java.util.*

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
class TimeDeserializer(vc: Class<*>?) : StdDeserializer<Date>(vc) {

	private val log = LogFactory.getLog(javaClass)

	private constructor() : this(Date::class.java)

	override fun deserialize(jp: JsonParser?,
	                         ctx: DeserializationContext?): Date? {
		val codec = jp!!.codec
		val jsonNode: JsonNode = codec.readTree(jp)
		val stringVal = jsonNode.asText()
		log.debug("${javaClass.name}: $stringVal")
		return if (!StringUtils.hasText(stringVal)) null else FormatUtils.decodeDate(stringVal)
	}
}