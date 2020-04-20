package pinboard.format.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.BDDAssertions
import org.junit.jupiter.api.Test
import pinboard.Bookmark
import java.util.*

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
class TagSerializationTest {

    private val objectMapper = ObjectMapper()

    @Test
    fun deserializeTags() {

        val json = """ {"href":"http://a-bookmark.com/a/b/c","description":"description","hash":"e29y29ds","meta":"meta","extended":"extended",
                                    "tags":"spring kotlin","time":"2017-08-02T23:04:16Z","shared":"yes","toread":"no"}
                                """
        val bookmark = this.objectMapper.readValue <Bookmark>(json, Bookmark::class.java)
        BDDAssertions.then(bookmark.tags).contains("spring", "kotlin")
    }

    @Test
    fun serializeTags() {
        val bookmark = Bookmark(("http://a-bookmark.com/a/b/c"), "description",
                "extended", "e29y29ds", "meta", Date(), true, false, arrayOf("spring", "kotlin"))
        val string = this.objectMapper.writeValueAsString(bookmark)
        BDDAssertions.then(string).contains(""" "spring kotlin"  """.trim())
    }
}