package com.hyuuny.norja.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisRepository {

    lateinit var redisTemplate: RedisTemplate<String, Any>

    operator fun set(key: String, o: Any, minutes: Long) {
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(o.javaClass)
        redisTemplate.opsForValue()[key, o, minutes] = TimeUnit.MINUTES
    }

    operator fun get(key: String?): Any = redisTemplate.opsForValue()[key!!]!!

    fun hasKey(key: String): Boolean = redisTemplate.hasKey(key)

}