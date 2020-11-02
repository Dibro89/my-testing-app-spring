package ua.training.mytestingapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.training.mytestingapp.entity.Option;
import ua.training.mytestingapp.entity.Question;
import ua.training.mytestingapp.entity.Test;
import ua.training.mytestingapp.service.TestService;

import java.util.Map;

@Controller
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestEditController {

    private final TestService testService;
    private final ObjectMapper objectMapper;

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showTestCreateForm() {
        return "test_edit";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public Map<String, Object> createTest(
        @RequestBody Test test
    ) {
        for (Question question : test.getQuestions()) {
            for (Option option : question.getOptions()) {
                option.setQuestion(question);
            }
            question.setTest(test);
        }

        testService.save(test);

        return Map.of("testId", test.getId());
    }

    @GetMapping("/{testId}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showTestEditForm(
        Model model,
        @PathVariable Long testId
    ) throws JsonProcessingException {
        Test test = testService.findById(testId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("test", objectMapper.writeValueAsString(test));
        model.addAttribute("testId", testId);

        return "test_edit";
    }

    @PostMapping("/{testId}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public Map<String, Object> editTest(
        @PathVariable Long testId,
        @RequestBody Test editedTest
    ) {
        Test test = testService.findById(testId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        test.setName(editedTest.getName());
        test.setSubject(editedTest.getSubject());
        test.setDifficulty(editedTest.getDifficulty());
        test.setDuration(editedTest.getDuration());

        for (Question question : editedTest.getQuestions()) {
            for (Option option : question.getOptions()) {
                option.setQuestion(question);
            }
            question.setTest(test);
        }

        test.getQuestions().clear();
        test.getQuestions().addAll(editedTest.getQuestions());

        testService.save(test);

        return Map.of("testId", test.getId());
    }

    @GetMapping("/{testId}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(
        @PathVariable Long testId
    ) {
        Test test = testService.findById(testId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        testService.delete(test);

        return "redirect:/";
    }
}
