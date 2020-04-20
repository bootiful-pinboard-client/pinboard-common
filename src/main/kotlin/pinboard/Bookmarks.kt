package pinboard

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import pinboard.format.jackson.TimeDeserializer
import pinboard.format.jackson.TimeSerializer
import java.util.*

open class Bookmarks(date: Date?, var user: String?, var posts: Array<Bookmark>) {

	private constructor() : this(null, null, emptyArray())

	@JsonSerialize(using = TimeSerializer::class)
	@JsonDeserialize(using = TimeDeserializer::class)
	var date: Date? = date
}