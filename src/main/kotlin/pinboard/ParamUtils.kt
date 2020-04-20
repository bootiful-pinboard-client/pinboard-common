package pinboard

import org.springframework.util.Assert
import pinboard.format.FormatUtils
import java.util.*
import kotlin.collections.LinkedHashMap


/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
object ParamUtils {

	fun exchangeParameters(token: String, vararg pairs: Pair<String, Any?>): Map<String, String> {

		val inputMap = pairs.toMap(LinkedHashMap(pairs.size))
		val outputMap = mutableMapOf("auth_token" to token, "format" to "json")
		Assert.isTrue(outputMap.size == 2) { "there should be at least two elements in the outputMap at this point" }

		val dateTransformer = { d: Date -> FormatUtils.encodeDate(d) }
		val tagTransformer = { t: Array<String> -> t.joinToString(" ") }
		val intTransformer = { t: Int -> t.toString() }
		val boolTransformer = { b: Boolean -> if (b) "yes" else "no" }

		contributeParameter("url", inputMap, outputMap, null, null)
		contributeParameter("shared", inputMap, outputMap, null, boolTransformer)
		contributeParameter("toread", inputMap, outputMap, null, boolTransformer)
		contributeParameter("replace", inputMap, outputMap, null, boolTransformer)
		contributeParameter("extended", inputMap, outputMap, null, null)
		contributeParameter("dt", inputMap, outputMap, null, dateTransformer)
		contributeParameter("description", inputMap, outputMap, null, null)
		contributeParameter("tag", inputMap, outputMap, null, tagTransformer)
		contributeParameter("tags", inputMap, outputMap, null, tagTransformer)
		contributeParameter("start", inputMap, outputMap, null, intTransformer)
		contributeParameter("results", inputMap, outputMap, null, intTransformer)
		contributeParameter("meta", inputMap, outputMap, null, intTransformer)
		contributeParameter("fromdt", inputMap, outputMap, null, dateTransformer)
		contributeParameter("todt", inputMap, outputMap, null, dateTransformer)
		contributeParameter("count", inputMap, outputMap, null, intTransformer)
		return outputMap.toSortedMap().let {
//			it.keys.forEach { println(it ) }
			it
		}
	}


	private fun <T> contributeParameter(key: String,
	                                    inputMap: MutableMap<String, Any?>,
	                                    outputMap: MutableMap<String, String>,
	                                    default: T? = null,
	                                    stringTransformer: ((T) -> String)? = null) {

		val elementFromMapTypedAsT: T? =
				if (inputMap.containsKey(key)) inputMap[key] as T else default // use getOrDefault
		if (elementFromMapTypedAsT != null) {
			if (stringTransformer != null) {
				outputMap.put(key, stringTransformer(elementFromMapTypedAsT))
			} else {
				outputMap.put(key, elementFromMapTypedAsT.toString())
			}
		}
	}

}