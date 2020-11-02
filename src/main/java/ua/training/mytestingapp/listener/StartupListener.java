package ua.training.mytestingapp.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.training.mytestingapp.service.TestService;

@Component
@RequiredArgsConstructor
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private final TestService testService;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        testService.createSampleTest();
    }
}
