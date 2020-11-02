package ua.training.mytestingapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.training.mytestingapp.entity.Test;
import ua.training.mytestingapp.service.TestService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private static final List<String> SORTS = List.of("byName", "byDifficulty", "byAttempts");

    private final TestService testService;

    @GetMapping
    public String home(
        Model model,
        Optional<String> subject,
        Optional<String> sort
    ) {
        List<Test> tests = testService.findAllBySubject(
            subject,
            sort.filter(SORTS::contains).orElse("byName")
        );

        model.addAttribute("tests", tests);
        model.addAttribute("subjects", testService.findAllSubjects());
        model.addAttribute("sorts", SORTS);

        return "home";
    }
}
