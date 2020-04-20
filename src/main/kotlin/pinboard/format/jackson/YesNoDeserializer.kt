package pinboard.format.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import pinboard.format.FormatUtils

/**
 * @author [Josh Long](josh@joshlong.com)
 */
class YesNoDeserializer(vc: Class<*>?) : StdDeserializer<Boolean>(vc) {

    private constructor() : this(Boolean::class.java)

    override fun deserialize(jp: JsonParser?,
                             deserializationContext: DeserializationContext?): Boolean {
        val codec = jp!!.codec
        val jsonNode: JsonNode = codec.readTree(jp)
        val stringVal = jsonNode.asText()
        return FormatUtils.decodeYesOrNo(stringVal)
    }
}