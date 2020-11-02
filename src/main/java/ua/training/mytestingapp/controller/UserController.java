package ua.training.mytestingapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.training.mytestingapp.entity.User;
import ua.training.mytestingapp.entity.projection.UserListItemProjection;
import ua.training.mytestingapp.service.UserService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String showUsersView() {
        return "users";
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> getUsers(
        Optional<String> username,
        @RequestParam(defaultValue = "0") int page
    ) {
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
    public String showUserInfo(
        @AuthenticationPrincipal User user,
        @PathVariable String username,
        Model model
    ) {
        User targetUser = userService.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("userInfo", buildUserInfo(targetUser));
        model.addAttribute("attempts", targetUser.getAttempts());

        model.addAttribute("canBlock", user != null && user.isAdmin());
        model.addAttribute("canEdit", user != null && (user.isAdmin() || user.getId().equals(targetUser.getId())));
        model.addAttribute("blocked", targetUser.isLocked());

        return "user";
    }

    private static Map<String, Object> buildUserInfo(User user) {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put("id", user.getId());
        ret.put("username", user.getUsername());
        ret.put("displayName", user.getDisplayName());
        ret.put("registrationDate", user.getRegistrationDate());
        return ret;
    }
}
