package ua.training.mytestingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.training.mytestingapp.dto.AttemptForm;
import ua.training.mytestingapp.entity.*;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AttemptService {

    private final UserService userService;
    private final TestService testService;

    @Transactional
    public Attempt checkAttempt(AttemptForm form) {
        User user = userService.findById(form.getUserId())
            .orElseThrow();

        Test test = testService.findById(form.getTestId())
            .orElseThrow();

        long count = form.getParameterMap().entrySet().stream()
            .filter(entry -> checkQuestion(test, entry))
            .count();

        String score = count + "/" + test.getQuestions().size();

        return new Attempt(user, test, score);
    }

    private static boolean checkQuestion(Test test, Map.Entry<String, String[]> answerEntry) {
        long questionId = Long.parseLong(answerEntry.getKey());

        Question question = getQuestionInTest(test, questionId)
            .orElseThrow();

        Set<Long> answers = Stream.of(answerEntry.getValue())
            .map(Long::parseLong)
            .collect(Collectors.toSet());

        Set<Long> correctAnswers = question.getOptions().stream()
            .filter(Option::isCorrect)
            .map(Option::getId)
            .collect(Collectors.toSet());

        return answers.equals(correctAnswers);
    }

    private static Optional<Question> getQuestionInTest(Test test, long questionId) {
        for (Question question : test.getQuestions()) {
            if (question.getId() == questionId) {
                return Optional.of(question);
            }
        }

        return Optional.empty();
    }
}
