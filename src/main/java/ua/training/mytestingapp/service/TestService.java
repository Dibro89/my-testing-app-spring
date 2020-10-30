package ua.training.mytestingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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

    public Optional<Test> findById(Long id) {
        return testRepository.findById(id);
    }

    public List<String> findAllSubjects() {
        return testRepository.findAllSubjects();
    }

    public List<Test> findAllBySubject(Optional<String> subject, Optional<Sort> sort) {
        if (subject.isPresent()) {
            if (sort.isPresent()) {
                return testRepository.findAllBySubject(subject.get(), sort.get());
            } else {
                return testRepository.findAllBySubject(subject.get());
            }
        } else {
            if (sort.isPresent()) {
                return testRepository.findAll(sort.get());
            } else {
                return testRepository.findAll();
            }
        }
    }

    @Transactional
    public void createSampleTest() {
        Test test = new Test();
        test.setName("hello test");
        test.setSubject("hello subject");
        test.setDuration(30);
        test.setDifficulty(5);

        Question question1 = new Question();
        question1.setTest(test);
        question1.setText("hello question1");
        question1.setMultiple(false);

        Option option11 = new Option();
        option11.setQuestion(question1);
        option11.setText("hello option11");
        option11.setCorrect(true);

        Option option12 = new Option();
        option12.setQuestion(question1);
        option12.setText("hello option12");
        option12.setCorrect(false);

        Option option13 = new Option();
        option13.setQuestion(question1);
        option13.setText("hello option13");
        option13.setCorrect(false);

        question1.setOptions(List.of(option11, option12, option13));

        Question question2 = new Question();
        question2.setTest(test);
        question2.setText("hello question2");
        question2.setMultiple(true);

        Option option21 = new Option();
        option21.setQuestion(question2);
        option21.setText("hello option21");
        option21.setCorrect(false);

        Option option22 = new Option();
        option22.setQuestion(question2);
        option22.setText("hello option22");
        option22.setCorrect(true);

        Option option23 = new Option();
        option23.setQuestion(question2);
        option23.setText("hello option23");
        option23.setCorrect(true);

        question2.setOptions(List.of(option21, option22, option23));

        test.setQuestions(List.of(question1, question2));

        testRepository.save(test);
    }
}
