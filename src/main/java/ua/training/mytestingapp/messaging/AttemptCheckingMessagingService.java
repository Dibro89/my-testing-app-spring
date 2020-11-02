package ua.training.mytestingapp.messaging;

import ua.training.mytestingapp.dto.AttemptForm;

public interface AttemptCheckingMessagingService {

    void submit(AttemptForm form);
}
