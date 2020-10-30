package ua.training.mytestingapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import ua.training.mytestingapp.entity.Attempt;
import ua.training.mytestingapp.entity.Question;
import ua.training.mytestingapp.entity.Test;
import ua.training.mytestingapp.entity.User;
import ua.training.mytestingapp.service.AttemptService;
import ua.training.mytestingapp.service.TestService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/tests")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final TestService testService;
    private final AttemptService attemptService;

    @GetMapping("/{testId}")
    @PreAuthorize("hasRole('USER')")
    public String info(Model model, @PathVariable Long testId) {
        Test test = testService.findById(testId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("testInfo", Map.of(
            "id", test.getId(),
            "name", test.getName(),
            "subject", test.getSubject(),
            "duration", test.getDuration(),
            "difficulty", test.getDifficulty(),
            "creationDate", test.getCreationDate()
        ));

        return "test";
    }

    @GetMapping("/{testId}/begin")
    @PreAuthorize("hasRole('USER')")
    public String begin(Model model, @PathVariable Long testId) {
        Test test = testService.findById(testId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("test", test);

        return "test_inprogress";
    }

    @PostMapping("/{testId}/end")
    @PreAuthorize("hasRole('USER')")
    public String end(
        @AuthenticationPrincipal User user,
        Model model,
        @PathVariable Long testId,
        HttpServletRequest request
    ) {
        Test test = testService.findById(testId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        long correctAnswerCount = request.getParameterMap().entrySet().stream()
            .filter(entry -> checkQuestion(test, entry))
            .count();

        String score = correctAnswerCount + " / " + test.getQuestions().size();

        model.addAttribute("result", Map.of("result", score));

        Attempt attempt = new Attempt();
        attempt.setUser(user);
        attempt.setTest(test);
        attempt.setScore(score);
        attemptService.save(attempt);

        return "test_result";
    }

    private boolean checkQuestion(Test test, Map.Entry<String, String[]> entry) {
        Question question = test.getQuestion(Long.parseLong(entry.getKey()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        Set<Long> answers = Stream.of(entry.getValue())
            .map(Long::parseLong)
            .collect(Collectors.toSet());

        return question.check(answers);
    }

    @PostConstruct
    public void postConstruct() {
        testService.createSampleTest();
    }
//
//    @GetMapping("/{testId}/edit")
////    @PreAuthorize()
//    public String edit(@PathVariable String testId) {
//        return "test_edit";
//    }
//
//    @PostMapping("/{testId}/edit")
////    @PreAuthorize()
//    public String edit2(@PathVariable String testId) {
//        return "redirect:/tests/s" + testId;
//    }
//
//    @PostMapping("/{testId}")
//    public String delete(@PathVariable String testId) {
//        return "redirect:/";
//    }
}

