package ua.training.mytestingapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.training.mytestingapp.dto.RegistrationForm;
import ua.training.mytestingapp.entity.User;
import ua.training.mytestingapp.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class UserRegistrationController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @ModelAttribute("form")
    public RegistrationForm form() {
        return new RegistrationForm();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "user_edit";
    }

    @PostMapping
    public String register(Model model, @Valid @ModelAttribute("form") RegistrationForm form, Errors errors) {
        if (errors.hasFieldErrors()) {
            List<String> fieldErrors = errors.getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.toList());
            model.addAttribute("errors", fieldErrors);
            return "user_edit";
        }

        if (userService.existsByUsername(form.getUsername())) {
            model.addAttribute("errors", List.of("username.exists"));
            return "user_edit";
        }

        User user = form.toUser(passwordEncoder);
        userService.save(user);

        return "redirect:/login?registered";
    }
}
