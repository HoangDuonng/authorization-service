package com.graduationproject.authorization_service.listener;

import com.graduationproject.authorization_service.event.UserCreatedEventForAuthorizationService;
import com.graduationproject.authorization_service.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedEventListener {

    private final UserRoleService userRoleService;

    @RabbitListener(queues = "${rabbitmq.queue.user-created-authorization}")
    public void handleUserCreatedEvent(UserCreatedEventForAuthorizationService event) {
        log.info("Received user created event for user: {}", event.getUserId());
        try {
            Long userId = event.getUserId();
            userRoleService.assignDefaultRole(userId, event.getRole());
            log.info("Successfully assigned role to user: {}", userId);
        } catch (Exception e) {
            log.error("Error assigning role to user: {}", event.getUserId(), e);
        }
    }
}
