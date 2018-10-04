package com.personservice.config;

import com.common.rabbitmq.RabbitMQRouting;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public DirectExchange personExchange() {
        return new DirectExchange(RabbitMQRouting.Exchange.PERSON.name());
    }

    @Bean
    public Queue personCreateQueue() {
        return new Queue("personCreateQueue");
    }

    @Bean
    public Binding bindingPersonCreated(DirectExchange personExchange, Queue personCreateQueue) {
        return BindingBuilder.bind(personCreateQueue).to(personExchange)
                .with(RabbitMQRouting.Person.CREATE);
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory containerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(jacksonConverter());
        return factory;
    }
}
