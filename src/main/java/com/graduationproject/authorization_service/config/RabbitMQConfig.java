package com.graduationproject.authorization_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.graduationproject.authorization_service.event.UserCreatedEventForAuthorizationService;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    
    @Value("${rabbitmq.queue.user-created-authorization}")
    private String queueUserCreated;

    @Value("${rabbitmq.exchange.user-events}")
    private String exchangeUserEvents;

    @Value("${rabbitmq.routing-key.user-created-authorization}")
    private String routingKeyUserCreatedAuthorization;


    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(exchangeUserEvents);
    }

    @Bean
    public Queue userCreatedQueue() {
        return new Queue(queueUserCreated);
    }

    @Bean
    public Binding userCreatedBinding(Queue userCreatedQueue, TopicExchange userEventsExchange) {
        return BindingBuilder.bind(userCreatedQueue)
                .to(userEventsExchange)
                .with(routingKeyUserCreatedAuthorization);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put(
                "com.graduationproject.user_service.event.UserCreatedEventForAuthorizationService",
                UserCreatedEventForAuthorizationService.class);
        typeMapper.setIdClassMapping(idClassMapping);
        typeMapper.setTypePrecedence(DefaultJackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}