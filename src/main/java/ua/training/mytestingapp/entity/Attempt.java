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
}
