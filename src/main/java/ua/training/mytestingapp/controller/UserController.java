package ua.training.mytestingapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.training.mytestingapp.entity.User;
import ua.training.mytestingapp.entity.projection.UserListItemProjection;
import ua.training.mytestingapp.service.AttemptService;
import ua.training.mytestingapp.service.UserService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AttemptService attemptService;

    @GetMapping
    public String showUsersView() {
        return "users";
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> getUsers(Optional<String> username, @RequestParam(defaultValue = "0") int page) {
        Page<UserListItemProjection> result = userService.getUserList(username, page);
        return Map.of(
            "users", result.getContent(),
            "pagination", Map.of(
                "page", result.getNumber(),
                "totalPages", result.getTotalPages()
            )
        );
    }

    @GetMapping("/{username}")
    public String showUserInfo(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Map<String, Object> userInfo = new LinkedHashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("displayName", user.getDisplayName());
        userInfo.put("registrationDate", user.getRegistrationDate());

        model.addAttribute("userInfo", userInfo);

        model.addAttribute("attempts", attemptService.findByUser(user));

        return "user";
    }
}
