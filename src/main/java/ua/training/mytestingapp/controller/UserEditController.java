package ua.training.mytestingapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.training.mytestingapp.dto.UserEditForm;
import ua.training.mytestingapp.entity.User;
import ua.training.mytestingapp.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users/{username}")
@RequiredArgsConstructor
public class UserEditController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @ModelAttribute("targetUser")
    public User targetUser(
        @PathVariable String username
    ) {
        return userService.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @ModelAttribute("form")
    public UserEditForm form() {
        return new UserEditForm();
    }

    @GetMapping("/edit")
    @PreAuthorize("hasRole('ADMIN') || (#user != null && #user.id == #targetUser.id)")
    public String showEditForm(
        @AuthenticationPrincipal User user,
        @ModelAttribute("targetUser") User targetUser
    ) {
        return "user_edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ADMIN') || (#user != null && #user.id == #targetUser.id)")
    public String edit(
        Model model,
        @AuthenticationPrincipal User user,
        @ModelAttribute("targetUser") User targetUser,
        @Valid @ModelAttribute("form") UserEditForm form,
        Errors errors
    ) {
        if (errors.hasFieldErrors()) {
            List<String> fieldErrors = errors.getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.toList());
            model.addAttribute("errors", fieldErrors);
            return "user_edit";
        }

        targetUser.setDisplayName(form.getDisplayName());
        targetUser.setPassword(passwordEncoder.encode(form.getPassword()));

        if (user.isAdmin()) {
            targetUser.setAdmin(form.isAdmin());
        }

        userService.save(targetUser);

        return "redirect:/users/" + targetUser.getUsername();
    }

    @GetMapping("/block")
    @PreAuthorize("hasRole('ADMIN')")
    public String block(
        @ModelAttribute("targetUser") User target
    ) {
        target.setLocked(!target.isLocked());
        userService.save(target);
        return "redirect:/users/" + target.getUsername();
    }
}
