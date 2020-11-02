package ua.training.mytestingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.training.mytestingapp.entity.Option;
import ua.training.mytestingapp.entity.Question;
import ua.training.mytestingapp.entity.Test;
import ua.training.mytestingapp.repository.TestRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public Test save(Test test) {
        return testRepository.save(test);
    }

    public List<Test> findAllBySubject(Optional<String> subject, String sort) {
        if (subject.isPresent()) {
            switch (sort) {
                case "byName":
                    return testRepository.findAllBySubjectOrderByName(subject.get());
                case "byDifficulty":
                    return testRepository.findAllBySubjectOrderByDifficulty(subject.get());
                case "byAttempts":
                    return testRepository.findAllBySubjectOrderByAttempts(subject.get());
            }
        } else {
            switch (sort) {
                case "byName":
                    return testRepository.findAllByOrderByName();
                case "byDifficulty":
                    return testRepository.findAllByOrderByDifficulty();
                case "byAttempts":
                    return testRepository.findAllByOrderByAttempts();
            }
        }

        throw new IllegalArgumentException("sort");
    }

    public Optional<Test> findById(Long id) {
        return testRepository.findById(id);
    }

    public List<String> findAllSubjects() {
        return testRepository.findAllSubjects();
    }

    public void delete(Test test) {
        testRepository.delete(test);
    }

    @Transactional
    public void createSampleTest() {
        if (testRepository.existsById(1L)) {
            return;
        }

        Test test = new Test();
        test.setName("Sample test");
        test.setSubject("Sample subject");
        test.setDuration(30);
        test.setDifficulty(5);

        Question question1 = new Question();
        question1.setTest(test);
        question1.setText("Sample question?");
        question1.setMultiple(false);

        Option option11 = new Option();
        option11.setQuestion(question1);
        option11.setText("Sample option");
        option11.setCorrect(true);

        Option option12 = new Option();
        option12.setQuestion(question1);
        option12.setText("Sample option");
        option12.setCorrect(false);

        Option option13 = new Option();
        option13.setQuestion(question1);
        option13.setText("Sample option");
        option13.setCorrect(false);

        question1.setOptions(List.of(option11, option12, option13));

        Question question2 = new Question();
        question2.setTest(test);
        question2.setText("Sample question?");
        question2.setMultiple(true);

        Option option21 = new Option();
        option21.setQuestion(question2);
        option21.setText("Sample option");
        option21.setCorrect(false);

        Option option22 = new Option();
        option22.setQuestion(question2);
        option22.setText("Sample option");
        option22.setCorrect(true);

        Option option23 = new Option();
        option23.setQuestion(question2);
        option23.setText("Sample option");
        option23.setCorrect(true);

        question2.setOptions(List.of(option21, option22, option23));

        test.setQuestions(List.of(question1, question2));

        testRepository.save(test);
    }
}
