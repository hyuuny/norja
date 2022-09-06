package com.hyuuny.norja.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.util.ObjectUtils
import redis.embedded.RedisServer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Profile("test")
@Configuration
class EmbeddedRedisConfig {

    @Value("\${spring.redis.port}")
    private val redisPort = 0

    private var redisServer: RedisServer? = null

    @PostConstruct
    @Throws(IOException::class)
    fun redisServer() {
        val port = if (isRedisRunning()) findAvailablePort() else redisPort
        redisServer = RedisServer(port)
        redisServer!!.start()
    }

    @PreDestroy
    fun stopRedis() {
        if (redisServer != null) {
            redisServer!!.stop()
        }
    }

    /**
     * Embedded Redis가 현재 실행중인지 확인
     */
    @Throws(IOException::class)
    private fun isRedisRunning(): Boolean {
        return isRunning(executeGrepProcessCommand(redisPort)!!)
    }

    /**
     * 현재 PC/서버에서 사용가능한 포트 조회
     */
    @Throws(IOException::class)
    fun findAvailablePort(): Int {
        for (port in 10000..65535) {
            val process = executeGrepProcessCommand(port)
            if (!isRunning(process!!)) {
                return port
            }
        }
        throw IllegalArgumentException("Not Found Available port: 10000 ~ 65535")
    }


    /**
     * 해당 port를 사용중인 프로세스 확인하는 sh 실행
     */
    @Throws(IOException::class)
    private fun executeGrepProcessCommand(port: Int): Process? {
        val command = String.format("netstat -nat | grep LISTEN|grep %d", port)
        val shell = arrayOf("/bin/sh", "-c", command)
        return Runtime.getRuntime().exec(shell)
    }

    /**
     * 해당 Process가 현재 실행중인지 확인
     */
    private fun isRunning(process: Process): Boolean {
        var line: String?
        val pidInfo = StringBuilder()
        try {
            BufferedReader(
                InputStreamReader(process.inputStream)
            ).use { input ->
                while (input.readLine().also {
                        line = it
                    } != null) {
                    pidInfo.append(line)
                }
            }
        } catch (e: Exception) {
        }
        return !ObjectUtils.isEmpty(pidInfo.toString())
    }

}