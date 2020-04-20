package pinboard.format.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import pinboard.format.FormatUtils

/**
 * @author <a href="maito:josh@joshlong.com">Josh Long</a>
 */
class YesNoSerializer(t: Class<Boolean>?) : StdSerializer<Boolean>(t) {

    private constructor() : this(Boolean::class.java)

    override fun serialize(value: Boolean?,
                           gen: JsonGenerator?,
                           provider: SerializerProvider?) {
        gen!!.writeString(FormatUtils.encodeYesOrNo(value!!))
    }
}