package ua.training.mytestingapp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Entity
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Test test;

    private String text;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true,
        fetch = FetchType.EAGER)
    private List<Option> options;

    private boolean multiple;

    public boolean check(Set<Long> answers) {
        Set<Long> correctAnswers = getOptions().stream()
            .filter(Option::isCorrect)
            .map(Option::getId)
            .collect(Collectors.toSet());

        return answers.equals(correctAnswers);
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", text='" + text + '\'' +
            ", options=" + options +
            ", multiple=" + multiple +
            '}';
    }
}
