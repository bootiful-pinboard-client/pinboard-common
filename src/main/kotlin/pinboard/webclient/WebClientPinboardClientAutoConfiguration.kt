package pinboard.webclient

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pinboard.PinboardProperties

@Configuration
@EnableConfigurationProperties(PinboardProperties::class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
class WebClientPinboardClientAutoConfiguration(
		private val properties: PinboardProperties) {

	init {
		println("the application is a Webflux-based application.")
	}


	@Bean
	@ConditionalOnMissingBean
	fun webClientPinboardClient() : WebClientPinboardClient =
			WebClientPinboardClient(properties.token!!)
}