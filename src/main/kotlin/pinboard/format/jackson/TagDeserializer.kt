package pinboard.format.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
class TagDeserializer(vc: Class<*>?) : StdDeserializer<Array<String>>(vc) {

    private constructor() : this(Array<String>::class.java)

    override fun deserialize(jp: JsonParser?,
                             ctx: DeserializationContext?): Array<String> {
        val codec = jp!!.codec
        val jsonNode: JsonNode = codec.readTree(jp)
        val stringVal = jsonNode.asText()

        if (stringVal == "") {
            return emptyArray()
        }
        if (!stringVal.contains(' ')) {
            return arrayOf(stringVal)
        }
        return stringVal.split(' ').toTypedArray()
    }
}