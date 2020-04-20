package pinboard.format.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
class TagSerializer(t: Class<Array<String>>?) : StdSerializer<Array<String>>(t) {

    private constructor() : this(Array<String>::class.java)

    override fun serialize(tags: Array<String>?,
                           gen: JsonGenerator?, p2: SerializerProvider?) {

        val tagsString = tags!!.joinToString(" ")
        gen!!.writeString(tagsString)
    }
}