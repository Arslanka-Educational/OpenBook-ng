package org.example.kafka

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class Consumer {

    @KafkaListener(topics = ["my-topic"], groupId = "your-group-id")
    fun consume(message: String) {
        println("Consumed message: $message")
    }
}