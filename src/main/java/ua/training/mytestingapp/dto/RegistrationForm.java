package ua.training.mytestingapp.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.training.mytestingapp.entity.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class RegistrationForm extends EditForm {

    @NotNull
    @Pattern(regexp = "\\w{5,20}")
    private String username;

    public User toUser(PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername(getUsername());
        user.setPassword(passwordEncoder.encode(getPassword()));
        user.setDisplayName(getDisplayName());
        return user;
    }
}
