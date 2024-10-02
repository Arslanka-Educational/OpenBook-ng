import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.concurrent.ScheduledFuture

@Service
class RetryableTaskScheduler(
    private val taskScheduler: TaskScheduler,
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