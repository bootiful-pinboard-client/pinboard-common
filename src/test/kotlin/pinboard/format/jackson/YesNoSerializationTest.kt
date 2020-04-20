package pinboard.format.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.BDDAssertions
import org.junit.jupiter.api.Test
import pinboard.Bookmark
import java.util.*

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
class YesNoSerializationTest {

    private val objectMapper = ObjectMapper()

    @Test
    fun deserializeWithRegularObjectMapper() {
        val json =  """  
                    {"href":"http://a-bookmark.com/a/b/c","description":"description","hash":"e29y29ds","meta":"meta","extended":"extended",
                        "tags":"spring kotlin","time":"2017-08-02T23:04:16Z","shared":"yes","toread":"no"}
                    """
        val tree: Bookmark = this.objectMapper.readValue<Bookmark>(json, Bookmark::class.java)
        BDDAssertions.then(tree.shared).isEqualTo(true)
        BDDAssertions.then(tree.toread).isEqualTo(false)
    }

    @Test
    fun serializeWithRegularObjectMapper() {
        val bookmark = Bookmark(("http://a-bookmark.com/a/b/c"), "description",
                "extended", "e29y29ds", "meta", Date(), shared = true, toread = false, tags = arrayOf("twis", "kotlin"))
        val result = this.objectMapper.writeValueAsString(bookmark);
        BDDAssertions.then(result).contains("""  "shared":"yes"  """.trim())
        BDDAssertions.then(result).contains("""  "toread":"no"  """.trim())
    }
}