package ua.training.mytestingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Test test;

    private String text;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Option> options;

    private boolean multiple;

    public void addOption(Option option) {
        if (options == null) {
            options = new ArrayList<>();
        }
        options.add(option);
        option.setQuestion(this);
    }
}
