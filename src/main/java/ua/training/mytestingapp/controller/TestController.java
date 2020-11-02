package ua.training.mytestingapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import ua.training.mytestingapp.dto.AttemptForm;
import ua.training.mytestingapp.entity.Test;
import ua.training.mytestingapp.entity.User;
import ua.training.mytestingapp.messaging.impl.JmsAttemptCheckingMessagingService;
import ua.training.mytestingapp.service.TestService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    private final JmsTemplate jmsTemplate;

    @GetMapping("/{testId}")
    @PreAuthorize("hasRole('USER')")
    public String info(
        Model model,
        @PathVariable Long testId
    ) {
        Test test = testService.findById(testId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("testInfo", buildTestInfo(test));

        return "test";
    }

    @GetMapping("/{testId}/begin")
    @PreAuthorize("hasRole('USER')")
    public String begin(
        Model model,
        @PathVariable Long testId
    ) {
        Test test = testService.findById(testId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("test", test);

        return "test_inprogress";
    }

    @PostMapping("/{testId}/end")
    @PreAuthorize("hasRole('USER')")
    public String end(
        @AuthenticationPrincipal User user,
        @PathVariable Long testId,
        HttpServletRequest request
    ) {
        Test test = testService.findById(testId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        AttemptForm form = new AttemptForm();
        form.setUserId(user.getId());
        form.setTestId(test.getId());
        form.setParameterMap(request.getParameterMap());

        jmsTemplate.convertAndSend(JmsAttemptCheckingMessagingService.DESTINATION_NAME, form);

        return "test_result";
    }

    private static Map<String, Object> buildTestInfo(Test test) {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put("id", test.getId());
        ret.put("name", test.getName());
        ret.put("subject", test.getSubject());
        ret.put("duration", test.getDuration());
        ret.put("difficulty", test.getDifficulty());
        ret.put("creationDate", test.getCreationDate());
        return ret;
    }
}
