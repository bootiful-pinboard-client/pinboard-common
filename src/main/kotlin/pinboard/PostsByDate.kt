package pinboard

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import pinboard.format.jackson.TagDeserializer
import pinboard.format.jackson.TagSerializer
import java.util.*

open class PostsByDate(var user: String?, tag: Array<String>?, val dates: Map<Date, Int>?) {

	private constructor() : this(null, emptyArray(), emptyMap())

	@JsonDeserialize(using = TagDeserializer::class)
	@JsonSerialize(using = TagSerializer::class)
	var tag = tag
}