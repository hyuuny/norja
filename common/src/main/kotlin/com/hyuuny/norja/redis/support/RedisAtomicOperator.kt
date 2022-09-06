package com.hyuuny.norja.redis.support

import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.support.atomic.RedisAtomicLong
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

@Component
class RedisAtomicOperator(
    private val redisConnectionFactory: RedisConnectionFactory,
) {

    private var redisOperations: RedisOperations<String, Long>? = null

    @PostConstruct
    fun postConstruct() {
        val redisTemplate = RedisTemplate<String, Long>()
        redisTemplate.keySerializer = RedisSerializer.string()
        redisTemplate.valueSerializer = GenericToStringSerializer(Long::class.java)
        redisTemplate.isExposeConnection = true
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        redisTemplate.afterPropertiesSet()
        this.redisOperations = redisTemplate
    }

    fun keys(pattern: String): Set<String>? {
        return redisOperations!!.keys(pattern)
    }

    fun delete(keys: Collection<String>?): Long? {
        return redisOperations!!.delete(keys!!)
    }

    operator fun get(key: String?): Long {
        val redisAtomicLong = RedisAtomicLong(key!!, redisOperations!!)
        return redisAtomicLong.get()
    }

    fun increment(key: String?): Long {
        val redisAtomicLong = RedisAtomicLong(key!!, redisOperations!!)
        return redisAtomicLong.incrementAndGet()
    }

    fun increment(key: String?, time: Long, timeUnit: TimeUnit?): Long {
        val redisAtomicLong = RedisAtomicLong(key!!, redisOperations!!)
        redisAtomicLong.expire(time, timeUnit!!)
        return redisAtomicLong.incrementAndGet()
    }

    fun increment(key: String?, expireAt: Instant?): Long {
        val redisAtomicLong = RedisAtomicLong(key!!, redisOperations!!)
        redisAtomicLong.expireAt(expireAt!!)
        return redisAtomicLong.incrementAndGet()
    }

    fun increment(key: String?, expireAt: Date?): Long {
        val redisAtomicLong = RedisAtomicLong(key!!, redisOperations!!)
        redisAtomicLong.expireAt(expireAt!!)
        return redisAtomicLong.incrementAndGet()
    }

    fun increment(key: String?, increment: Int, time: Long, timeUnit: TimeUnit?): Long {
        val redisAtomicLong = RedisAtomicLong(key!!, redisOperations!!)
        redisAtomicLong.expire(time, timeUnit!!)
        return redisAtomicLong.incrementAndGet()
    }

}