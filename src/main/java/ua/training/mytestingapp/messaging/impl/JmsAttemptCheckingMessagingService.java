package ua.training.mytestingapp.messaging.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ua.training.mytestingapp.dto.AttemptForm;
import ua.training.mytestingapp.messaging.AttemptCheckingMessagingService;

@Service
@RequiredArgsConstructor
public class JmsAttemptCheckingMessagingService implements AttemptCheckingMessagingService {

    public static final String DESTINATION_NAME = "testing.attempt";

    private final JmsTemplate jmsTemplate;

    @Override
    public void submit(AttemptForm form) {
        jmsTemplate.convertAndSend(DESTINATION_NAME, form);
    }
}
