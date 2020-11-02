package ua.training.mytestingapp.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Test test;

    private String score;

    public Attempt() {
    }

    public Attempt(User user, Test test, String score) {
        this.user = user;
        this.test = test;
        this.score = score;

        init();
    }

    private void init() {
        user.getAttempts().add(this);
        test.getAttempts().add(this);
    }
}
