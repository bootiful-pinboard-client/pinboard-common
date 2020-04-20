package pinboard

open class Notes(var count: Int? = 0, var notes: Array<Note>?) {
	private constructor() : this(0, arrayOf())
}