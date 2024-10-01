package org.example.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
class SchedulerConfig(
    @Value("\${spring.task-scheduler.pool-size}")
    private val poolSize: Int,
) {

    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        val taskScheduler = ThreadPoolTaskScheduler()
        taskScheduler.poolSize = this.poolSize
        taskScheduler.isRemoveOnCancelPolicy = true
        return taskScheduler
    }
}