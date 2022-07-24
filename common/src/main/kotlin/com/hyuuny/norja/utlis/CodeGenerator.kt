package com.hyuuny.norja.utlis

import com.fasterxml.uuid.Generators
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ofPattern
import java.util.concurrent.atomic.AtomicLong

object CodeGenerator {
    private val lastTime = AtomicLong(0);

    fun createCode() = this.nextId("R")

    fun nextId(prefix: String): String {
        var prev: Long
        var next = now().format(ofPattern("yyyyMMddHHmmssSSS")).toLong()
        do {
            prev = lastTime.get()
            next = ((if (next > prev) next else prev))
        } while (!lastTime.compareAndSet(prev, next))
        return prefix + next
    }
}