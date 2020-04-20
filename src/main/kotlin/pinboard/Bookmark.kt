package pinboard

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import pinboard.format.jackson.*
import java.util.*

open class Bookmark(var href: String?,
                    var description: String?,
                    var extended: String?,
                    var hash: String?,
                    var meta: String?,
                    time: Date?,
                    shared: Boolean,
                    toread: Boolean,
                    tags: Array<String>) {

	private constructor() : this(null, null, null, null, null, null, false, false, emptyArray())

	@JsonSerialize(using = TagSerializer::class)
	@JsonDeserialize(using = TagDeserializer::class)
	var tags: Array<String> = tags

	@JsonDeserialize(using = TimeDeserializer::class)
	@JsonSerialize(using = TimeSerializer::class)
	var time: Date? = time

	@JsonDeserialize(using = YesNoDeserializer::class)
	@JsonSerialize(using = YesNoSerializer::class)
	var shared: Boolean = shared

	@JsonDeserialize(using = YesNoDeserializer::class)
	@JsonSerialize(using = YesNoSerializer::class)
	var toread: Boolean = toread

}