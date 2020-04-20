package pinboard.webclient

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.logging.LogFactory
import org.springframework.boot.web.codec.CodecCustomizer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.util.Assert
import org.springframework.util.MimeType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.client.bodyToMono
import pinboard.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*


open class WebClientPinboardClient(private val token: String) {

	private val jacksonCodecCustomizer = CodecCustomizer {
		val objectMapper = ObjectMapper()
		val mimeTypes = arrayOf(MimeType.valueOf("text/plain;charset=utf-8"))
		val defaultCodecs = it.defaultCodecs()
		defaultCodecs.jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, *mimeTypes))
		defaultCodecs.jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, *mimeTypes))
	}

	private val client: WebClient = WebClient
			.builder()
			.codecs { codecConfigurer -> jacksonCodecCustomizer.customize(codecConfigurer) }
			.build()

	private val log = LogFactory.getLog(javaClass)

	private val pinboardApiEndpoint = "https://api.pinboard.in/v1"

	open fun updatePost(post: Bookmark) =
			updatePost(
					url = post.href!!,
					description = post.description!!,
					extended = post.extended!!,
					tags = post.tags,
					date = post.time!!,
					toread = post.toread,
					shared = post.shared
			)

	open fun updatePost(
			url: String,
			description: String,
			extended: String,
			tags: Array<String>,
			date: Date,
			shared: Boolean,
			toread: Boolean
	): Mono<Boolean> = this.addPost(
			url = url,
			description = description,
			replace = true,
			extended = extended,
			tags = tags,
			date = date,
			shared = shared,
			toread = toread)


	open fun addPost(url: String,
	                 description: String,
	                 extended: String,
	                 tags: Array<String>,
	                 date: Date,
	                 replace: Boolean,
	                 shared: Boolean,
	                 toread: Boolean): Mono<Boolean> {
		val params = ParamUtils.exchangeParameters(token, "url" to url, "description" to description, "extended" to extended,
				"tags" to tags, "dt" to date, "replace" to replace, "shared" to shared, "toread" to toread)
		val result = buildUrl("/posts/add", params)
		return isDone(this.client.get().uri(result, params))
	}

	private fun isDone(spec: WebClient.RequestHeadersSpec<*>): Mono<Boolean> {
		return spec.exchange().flatMap {
			val ok = it.statusCode().is2xxSuccessful
			it.bodyToMono<Map<String, String>>().map {
				val done = it.contains("done")
				done && ok
			}
		}
	}

	open fun deletePost(url: String): Mono<Boolean> {
		val params = ParamUtils.exchangeParameters(token, "url" to url)
		return isDone(this.client.get().uri(buildUrl("/posts/delete", params), params))
	}

	open fun getAllPosts(tag: Array<String>, start: Int = 0, results: Int = -1, fromDate: Date? = null, toDate: Date? = null, meta: Int = 0): Flux<Bookmark> {
		val parameters = ParamUtils.exchangeParameters(token, "tag" to tag, "start" to start, "results" to results, "fromdt" to fromDate, "todt" to toDate, "meta" to meta)
		val url = buildUrl("/posts/all", parameters)
		return this.client
				.get()
				.uri(url, parameters)
				.retrieve()
				.bodyToFlux()
	}

	open fun getRecentPosts(tag: Array<String>? = null, count: Int? = 15): Mono<Bookmarks> {
		val parameters = ParamUtils.exchangeParameters(token, "tag" to tag, "count" to count)
		val url = buildUrl("/posts/recent", parameters)
		return client.get().uri(url, parameters).retrieve().bodyToMono()
	}

	open fun getPosts(url: String? = null, dt: Date? = null,
	                  tag: Array<String>? = null, meta: Boolean? = null): Mono<Bookmarks> {
		Assert.isTrue(dt != null || url != null) { "you must specify either the date or the URL" }
		val parameters = ParamUtils.exchangeParameters(
				token, "tag" to tag, "url" to url, "meta" to meta, "dt" to dt)
		return this.client.get().uri(buildUrl("/posts/get", parameters), parameters).retrieve().bodyToMono<Bookmarks>()
	}

	private fun buildUrl(incomingUrl: String, params: Map<String, Any>): String {
		val map = params.entries.map { "${it.key}={${it.key}}" }
		val paramString = map.joinToString("&")
		return "${pinboardApiEndpoint}${incomingUrl}?${paramString}".let {
			log.info("the URL is $it")
			it
		}
	}

	open fun getCountOfPostsByDate(tag: Array<String>): Mono<PostsByDate> {
		val params = ParamUtils.exchangeParameters(token, "tag" to tag)
		val url = buildUrl("/posts/dates", params)
		return this.client.get().uri(url, params).retrieve().bodyToMono()
	}

	open fun suggestTagsForPost(url: String): Mono<SuggestedTags> {

		val params = ParamUtils.exchangeParameters(token, "url" to url)

		val uri = buildUrl("/posts/suggest", params)

		val suggestedTags = this.client
				.get()
				.uri(uri, params)
				.retrieve()
				.bodyToMono<Array<Map<String, Array<String>>>>()

		return suggestedTags.map { body ->
			val popular = "popular"
			val recommended = "recommended"
			val popularStrings = body.first { it.containsKey(popular) }[popular]
			val recommendedStrings = body.first { it.containsKey(recommended) }[recommended]
			SuggestedTags(popularStrings, recommendedStrings)
		}
	}

	open fun getUserTags(): Mono<Map<String, Int>> {
		val exchangeParameters = ParamUtils.exchangeParameters(token)
		val url = buildUrl("/tags/get", exchangeParameters)
		return this.client.get().uri(url, exchangeParameters).retrieve().bodyToMono()
	}

	open fun deleteTag(s: String): Mono<Boolean> {
		val params = ParamUtils.exchangeParameters(token, "tag" to arrayOf(s))
		val url = buildUrl("/tags/delete", params)
		val string = this.client.get().uri(url, params).retrieve().bodyToMono<String>()
		return string.map {
			it.toLowerCase().contains("done")
		}
	}

	open fun getUserSecret(): Mono<String> {
		val parameters = ParamUtils.exchangeParameters(token)
		val url = buildUrl("/user/secret", parameters)
		return this.client.get().uri(url, parameters).retrieve().bodyToMono()
	}

	open fun getApiToken(): Mono<String> {
		val params = ParamUtils.exchangeParameters(token)
		val url = buildUrl("/user/api_token/", params)
		return this.client.get().uri(url, params).retrieve().bodyToMono()
	}

	open fun getUserNotes(): Mono<Notes> {
		val params = ParamUtils.exchangeParameters(token)
		val url = buildUrl("/notes/list", params)
		return this.client.get().uri(url, params).retrieve().bodyToMono()
	}

	open fun getUserNote(id: String): Mono<Note> {
		val params = ParamUtils.exchangeParameters(token)
		val url = buildUrl("/notes/$id", params)
		return client.get().uri(url, params).retrieve().bodyToMono()
	}


}