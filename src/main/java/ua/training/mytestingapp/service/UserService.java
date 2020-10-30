package ua.training.mytestingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.training.mytestingapp.entity.User;
import ua.training.mytestingapp.entity.projection.UserListItemProjection;
import ua.training.mytestingapp.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    public static final int USERS_PER_PAGE = 10;

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Page<UserListItemProjection> getUserList(Optional<String> username, int page) {
        Pageable pageable = PageRequest.of(page, USERS_PER_PAGE);
        if (username.isPresent()) {
            return userRepository.findListItemsByUsernameContainingIgnoreCase(username.get(), pageable);
        } else {
            return userRepository.findListItemsBy(pageable);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @PostConstruct
    public void createAdmin() {
        final String adminUsername = "admin";
        final String adminPassword = "admin";
        final String adminDisplayName = "Admin";


        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setPassword(new BCryptPasswordEncoder().encode(adminPassword));
        admin.setDisplayName(adminDisplayName);
        admin.setAdmin(true);
        save(admin);
    }
}
