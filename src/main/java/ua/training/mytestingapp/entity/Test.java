package ua.training.mytestingapp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
public class Test {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String subject;

    private int duration;

    private int difficulty;

    private LocalDate creationDate;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    public Optional<Question> getQuestion(long id) {
        for (Question question : getQuestions()) {
            if (question.getId() == id) {
                return Optional.of(question);
            }
        }
        return Optional.empty();
    }

    @PrePersist
    public void prePersist() {
        if (creationDate == null) {
            creationDate = LocalDate.now();
        }
    }

    @Override
    public String toString() {
        return "Test{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", duration=" + duration +
            ", difficulty=" + difficulty +
            ", creationDate=" + creationDate +
            ", questions=" + questions +
            '}';
    }
}
