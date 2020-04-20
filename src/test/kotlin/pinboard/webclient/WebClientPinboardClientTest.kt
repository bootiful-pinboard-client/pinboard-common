package pinboard.webclient

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import pinboard.Bookmark
import reactor.kotlin.extra.math.min
import reactor.test.StepVerifier
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class WebClientPinboardClientTest {

	private val token = System.getenv("PINBOARD_TOKEN")
	private val reactivePinboardClient = WebClientPinboardClient(this.token)
	private val testTag = "pbctest"
	private val testTag2 = "pbctest2"
	private val bookmark = Bookmark("http://garfield.com", "description", "extended", "hash", "meta",
			Date(), true, true, arrayOf(this.testTag, this.testTag2))

	private val pinboardClientTestTags: Array<String> by lazy {
		bookmark.tags
	}

	@Test
	fun setupNewPost() {
		val insert = this.reactivePinboardClient
				.getAllPosts(tag = arrayOf(testTag))
				.flatMap { this.reactivePinboardClient.deletePost(it.href!!) }
				.thenMany(this.reactivePinboardClient
						.addPost(bookmark.href!!, bookmark.description!!, bookmark.extended!!, bookmark.tags, bookmark.time!!, true, true, true))
				.thenMany(this.reactivePinboardClient.getPosts(url = bookmark.href))
		StepVerifier
				.create(insert)
				.expectNextMatches { it.posts.firstOrNull() { it.href == this.bookmark.href } != null }
				.verifyComplete()
	}

	@AfterEach
	fun cleanup() {
		this.reactivePinboardClient
				.getAllPosts(tag = pinboardClientTestTags)
				.subscribe {
					reactivePinboardClient.deletePost(it.href!!)
				}
	}

	@Test
	fun getPosts() {
		val url = "https://twitter.com/VMware/status/1251683453994745857"
		StepVerifier
				.create(this.reactivePinboardClient.getPosts(url))
				.expectNextMatches {
					it.posts.first().href == url
				}
				.verifyComplete()
	}

	@Test
	fun getAllBookmarksByTag() {
		setupNewPost()
		val postsByTag = this.reactivePinboardClient.getAllPosts(tag = this.pinboardClientTestTags).take(10)
		StepVerifier
				.create(postsByTag)
				.expectNextCount(10)
				.verifyComplete()
	}

	@Test
	fun get10Records() {
		val maxResults = 10
		val postsByTag = this.reactivePinboardClient.getAllPosts( this.pinboardClientTestTags, 0, maxResults).take(12)
		StepVerifier
				.create(postsByTag)
				.expectNextCount(10)
				.verifyComplete()
	}

	@Test
	fun getTheLastTenDays() {
		val fromdt = Date.from(Instant.now().minus(10, ChronoUnit.DAYS))
		val postsByDate = this.reactivePinboardClient.getAllPosts(arrayOf("twis"), fromDate = fromdt)
		val comparator = Comparator<Bookmark> { a, b -> a.time!!.compareTo(b.time) }
		val minBookmark = postsByDate.min(comparator)
		StepVerifier
				.create(minBookmark)
				.expectNextMatches { it?.time?.after(fromdt)!! }
				.verifyComplete()
	}

	@Test
	fun deletePost() {

		StepVerifier
				.create(this.reactivePinboardClient.getAllPosts(bookmark.tags))
				.expectNextCount(1)
				.verifyComplete()
		this.reactivePinboardClient.deletePost(bookmark.href!!)

		StepVerifier
				.create(this.reactivePinboardClient.getAllPosts(bookmark.tags))
				.expectNextCount(0)
				.verifyComplete()
	}


	@Test
	fun getAllPosts() {
		val now = Instant.now()
		val then = now.minus(10, ChronoUnit.DAYS)
		val all = this.reactivePinboardClient.getAllPosts(tag = arrayOf("trump"), fromDate = Date(then.toEpochMilli()), toDate = Date(now.toEpochMilli())).take(10)
		StepVerifier
				.create(all.doOnNext { println(it.href) })
				.expectNextMatches { it.href != null }
				.expectNextCount(9)
				.verifyComplete()
	}

	@Test
	fun getRecentPostsByTag() {
		val tags = arrayOf("democrats")
		val result = this.reactivePinboardClient.getRecentPosts(tag = tags)
		StepVerifier
				.create(result)
				.expectNextMatches {
					it.user?.toLowerCase() == "starbuxman" && it.posts.isNotEmpty()
				}
				.verifyComplete()
	}

	@Test
	fun getNoOfPostsByDate() {
		setupNewPost()
		val result = this.reactivePinboardClient.getCountOfPostsByDate(arrayOf( this.bookmark.tags.first()))
		StepVerifier
				.create(result)
				.expectNextMatches {
					it.user!!.toLowerCase() == "starbuxman" && it.dates?.isNotEmpty()!! && it.tag?.isNotEmpty()!!
				}
				.verifyComplete()

	}

	@Test
	fun suggestTagsForPost() {
		val url = listOf("https://www.washingtonpost.com/world/national-security/",
				"you-cannot-say-that-to-the-press-trump-urged-mexican-president-",
				"to-end-his-public-defiance-on-border-wall-transcript-reveals/2017/",
				"08/03/0c2c0a4e-7610-11e7-8f39-eeb7d3a2d304_story.html?utm_term=.ea1119248010")
				.joinToString("")
		StepVerifier
				.create(this.reactivePinboardClient.suggestTagsForPost(url))
				.expectNextMatches {
					it.recommended!!.isNotEmpty()
				}
				.verifyComplete()
	}

	@Test
	fun tagFrequencyTable() {
		StepVerifier
				.create(reactivePinboardClient.getUserTags())
				.expectNextMatches {
					it["twis"] ?: error("couldn't find the key 'twis' in the userTags results") > 0
				}
				.verifyComplete()
	}

	@Test
	fun deleteTag() {
		setupNewPost()
		StepVerifier
				.create(this.reactivePinboardClient.deleteTag(this.pinboardClientTestTags.first()))
				.expectNextMatches {
					it == true
				}
				.verifyComplete()
	}

}