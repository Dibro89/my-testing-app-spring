package ua.training.mytestingapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.training.mytestingapp.entity.Test;
import ua.training.mytestingapp.service.TestService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private static final Map<String, Sort> SORTS = Map.of(
        "byName", Sort.by("name"),
        "byDifficulty", Sort.by("difficulty")
    );

    private final TestService testService;

    @GetMapping
    public String home(
        Model model,
        Optional<String> subject,
        @RequestParam(defaultValue = "byName") Optional<String> sort
    ) {
        List<Test> tests = testService.findAllBySubject(
            subject.filter(Predicate.not(String::isBlank)),
            sort.map(SORTS::get));
        model.addAttribute("tests", tests);

        List<String> subjects = testService.findAllSubjects();
        model.addAttribute("subjects", subjects);

        model.addAttribute("sorts", SORTS.keySet());

        return "home";
    }
}
