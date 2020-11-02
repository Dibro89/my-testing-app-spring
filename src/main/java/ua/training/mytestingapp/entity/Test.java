package ua.training.mytestingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String subject;

    private int duration;

    private int difficulty;

    private LocalDate creationDate;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @OneToMany(mappedBy = "test")
    @JsonIgnore
    private List<Attempt> attempts;

    public void addQuestion(Question question) {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        questions.add(question);
        question.setTest(this);
    }

    @PrePersist
    public void prePersist() {
        if (creationDate == null) {
            creationDate = LocalDate.now();
        }
    }
}
