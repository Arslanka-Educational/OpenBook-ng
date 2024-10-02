package org.example.service.schedule

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.ScheduledFuture

@Component
class RetryableTaskScheduler(
    private val taskScheduler: ThreadPoolTaskScheduler,
    @Value("\${spring.retry.max-attempts}")
    private val maxAttempts: Int,
    @Value("\${spring.retry.delay-between-retries-millis}")
    private val delayBetweenRetriesMillis: Long,
) {

    private val logger = LoggerFactory.getLogger(RetryableTaskScheduler::class.java)

    internal fun scheduleWithRetry(task: Runnable, triggerTime: Instant): ScheduledFuture<*> {
        return scheduleWithRetry(task, triggerTime, 0)
    }

    private fun scheduleWithRetry(task: Runnable, triggerTime: Instant, attempt: Int): ScheduledFuture<*> {
        return taskScheduler.schedule({
            try {
                task.run()
            } catch (e: Exception) {
                if (attempt + 1 < maxAttempts) {
                    val nextAttemptTime = Instant.now().plusMillis(delayBetweenRetriesMillis)
                    scheduleWithRetry(task, nextAttemptTime, attempt + 1)
                } else {
                    logger.error("Task failed after $maxAttempts attempts")
                }
            }
        }, triggerTime)
    }
}