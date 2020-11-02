package ua.training.mytestingapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.training.mytestingapp.dto.AttemptForm;
import ua.training.mytestingapp.entity.Attempt;
import ua.training.mytestingapp.entity.Option;
import ua.training.mytestingapp.entity.Question;
import ua.training.mytestingapp.service.AttemptService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MyTestingAppTests {

    @Autowired
    private AttemptService attemptService;

    @Test
    void contextLoads() {
    }

    @Test
    void attemptServiceChecksForm() {
        // Given
        var test = createTest();

        var form1 = createForm(Map.of(
            "1", new String[]{"12"},
            "2", new String[]{"21", "23"},
            "3", new String[]{"31"}
        ));

        var form2 = createForm(Map.of(
            "1", new String[]{"13"},
            "2", new String[]{"22"},
            "3", new String[]{"32"}
        ));

        var form3 = createForm(Map.of(
            "1", new String[]{"11"},
            "2", new String[]{},
            "3", new String[]{"33"}
        ));


        // When
        Attempt attempt1 = attemptService.check(test, form1);
        Attempt attempt2 = attemptService.check(test, form2);
        Attempt attempt3 = attemptService.check(test, form3);


        // Then
        assertEquals("2/3", attempt1.getScore());
        assertEquals("0/3", attempt2.getScore());
        assertEquals("1/3", attempt3.getScore());
    }

    private static ua.training.mytestingapp.entity.Test createTest() {
        var test = new ua.training.mytestingapp.entity.Test();

        var question1 = new Question();
        question1.setId(1L);
        question1.setMultiple(false);

        var option11 = new Option();
        option11.setId(11L);
        option11.setCorrect(false);
        question1.addOption(option11);

        var option12 = new Option();
        option12.setId(12L);
        option12.setCorrect(true);
        question1.addOption(option12);

        var option13 = new Option();
        option13.setId(13L);
        option13.setCorrect(false);
        question1.addOption(option13);

        test.addQuestion(question1);

        var question2 = new Question();
        question2.setId(2L);
        question2.setMultiple(true);

        var option21 = new Option();
        option21.setId(21L);
        option21.setCorrect(true);
        question2.addOption(option21);

        var option22 = new Option();
        option22.setId(22L);
        option22.setCorrect(false);
        question2.addOption(option22);

        var option23 = new Option();
        option23.setId(23L);
        option23.setCorrect(true);
        question2.addOption(option23);

        test.addQuestion(question2);

        var question3 = new Question();
        question3.setId(3L);
        question3.setMultiple(false);

        var option31 = new Option();
        option31.setId(31L);
        option31.setCorrect(false);
        question3.addOption(option31);

        var option32 = new Option();
        option32.setId(32L);
        option32.setCorrect(false);
        question3.addOption(option32);

        var option33 = new Option();
        option33.setId(33L);
        option33.setCorrect(true);
        question3.addOption(option33);

        test.addQuestion(question3);

        return test;
    }

    private static AttemptForm createForm(Map<String, String[]> parameterMap) {
        AttemptForm form = new AttemptForm();
        form.setParameterMap(parameterMap);
        return form;
    }
}
