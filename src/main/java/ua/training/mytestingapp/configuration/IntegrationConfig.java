package ua.training.mytestingapp.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.jms.support.converter.MessageConverter;
import ua.training.mytestingapp.messaging.impl.JmsAttemptCheckingMessagingService;
import ua.training.mytestingapp.service.AttemptService;

import javax.jms.ConnectionFactory;
import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private final AttemptService attemptService;

    @Bean
    public IntegrationFlow jmsToJpaIntegrationFlow(ConnectionFactory connectionFactory,
                                                   MessageConverter messageConverter,
                                                   EntityManagerFactory entityManagerFactory) {
        return IntegrationFlows
            .from(Jms
                .messageDrivenChannelAdapter(connectionFactory)
                .jmsMessageConverter(messageConverter)
                .destination(JmsAttemptCheckingMessagingService.DESTINATION_NAME))

            .transform(attemptService::checkAttempt)

            .handle(Jpa
                    .outboundAdapter(entityManagerFactory)
                    .persistMode(PersistMode.PERSIST),
                e -> e.transactional(true))
            .get();
    }
}
