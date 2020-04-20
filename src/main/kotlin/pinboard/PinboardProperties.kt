package pinboard

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@ConfigurationProperties(prefix = "pinboard")
class PinboardProperties(var token: String?) {

	constructor() : this(null)
}