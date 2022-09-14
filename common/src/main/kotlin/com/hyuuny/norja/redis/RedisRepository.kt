package com.hyuuny.norja.redis

import mu.KotlinLogging
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisRepository(
    private var redisTemplate: RedisTemplate<String, Any>,
) {

    private val log = KotlinLogging.logger {}

    operator fun set(key: String, o: Any, minutes: Long) {
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(o.javaClass)
        redisTemplate.opsForValue()[key, o, minutes] = TimeUnit.MINUTES
    }

    operator fun get(key: String?): Any = redisTemplate.opsForValue()[key!!]!!

    fun hasKey(key: String): Boolean = redisTemplate.hasKey(key)

    fun clear() {
        log.info("delete Cache...")
        try {
            redisTemplate.execute(RedisCallback { connection: RedisConnection ->
                connection.flushAll()
                null
            } as RedisCallback<*>)
        } catch (e: Exception) {
            log.info("delete Cache Fail")
        }
    }

}