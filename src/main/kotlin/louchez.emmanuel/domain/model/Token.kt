package louchez.emmanuel.domain.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant


data class AccessToken(val value: String, val expiresAt: Instant)
data class RefreshToken(val value: String, val expiresAt: Instant)