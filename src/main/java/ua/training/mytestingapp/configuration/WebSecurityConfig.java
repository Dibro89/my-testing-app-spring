package ua.training.mytestingapp.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import ua.training.mytestingapp.entity.User;
import ua.training.mytestingapp.service.UserService;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.formLogin()
            .loginPage("/login")
            .failureHandler(new CustomFailureHandler())
            .and()
            .logout()
            .logoutUrl("/logout");
    }

    @PostConstruct
    public void createAdmin() {
        final String adminUsername = "admin";
        final String adminPassword = "admin";

        if (userService.existsByUsername(adminUsername)) {
            return;
        }

        User user = new User();
        user.setUsername(adminUsername);
        user.setPassword(passwordEncoder().encode(adminPassword));
        user.setDisplayName(adminUsername.toUpperCase());
        user.addRole(User.ROLE_USER);
        user.addRole(User.ROLE_ADMIN);

        userService.save(user);
    }

    private static class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException exception) throws IOException, ServletException {
            boolean locked = exception instanceof LockedException;
            setDefaultFailureUrl(locked ? "/login?blocked" : "/login?error");

            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
