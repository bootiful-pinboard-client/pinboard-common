package pinboard

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import pinboard.format.jackson.NoteTimeDeserializer
import pinboard.format.jackson.NoteTimeSerializer
import java.util.*

open class Note(var id: String?,
                var title: String?,
                var length: Int?,
                created: Date?,
                updated: Date?,
                var hash: String?) {

	@JsonSerialize(using = NoteTimeSerializer::class)
	@JsonDeserialize(using = NoteTimeDeserializer::class)
	@JsonProperty("created_at")
	var created: Date? = created

	@JsonSerialize(using = NoteTimeSerializer::class)
	@JsonDeserialize(using = NoteTimeDeserializer::class)
	@JsonProperty("updated_at")
	var updated: Date? = updated

	private constructor() : this(null, null, null, null, null, null)
}