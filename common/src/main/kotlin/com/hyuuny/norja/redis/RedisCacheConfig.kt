package com.hyuuny.norja.redis

import mu.KotlinLogging
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.CacheKeyPrefix
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import java.lang.reflect.Method
import java.time.Duration


@EnableCaching
@Configuration
class RedisCacheConfig {

    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): RedisCacheManager {
        val redisConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .disableCachingNullValues()
            .entryTtl(Duration.ofMinutes(300))
            .computePrefixWith(CacheKeyPrefix.simple())
            .serializeValuesWith(SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
        val cacheConfigurations: Map<String, RedisCacheConfiguration> = mutableMapOf()

        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(connectionFactory)
            .cacheDefaults(redisConfiguration)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build()
    }

    @Bean
    fun customKeyGenerator(): CustomKeyGenerator = CustomKeyGenerator()

    class CustomKeyGenerator : KeyGenerator {
        private val log = KotlinLogging.logger {}

        override fun generate(target: Any, method: Method, vararg params: Any): Any {
            // class name. method name. parameter value
            val keySuffix =
                target.javaClass.simpleName + "." + method.name + "." + params.contentToString()
            log.info("Cache key suffix : {}", keySuffix)
            return keySuffix
        }
    }

}