package ua.training.mytestingapp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class Option {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Question question;

    private String text;

    private boolean correct;

    @Override
    public String toString() {
        return "Option{" +
            "id=" + id +
            ", text='" + text + '\'' +
            ", correct=" + correct +
            '}';
    }
}
