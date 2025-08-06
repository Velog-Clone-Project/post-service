package com.example.postservice.event;

import com.example.postservice.config.RabbitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostEventPublisher {

    private final AmqpTemplate amqpTemplate;
    private final RabbitProperties properties;

    public void sendPostDeletedEvent(Long postId) {
        amqpTemplate.convertAndSend(
                properties.getExchanges().getPost(),
                properties.getRoutingKeys().getPost().getDeleted(),
                new PostDeletedEvent(postId)
        );
    }
}
