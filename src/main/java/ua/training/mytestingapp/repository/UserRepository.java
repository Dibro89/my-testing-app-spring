package ua.training.mytestingapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.training.mytestingapp.entity.User;
import ua.training.mytestingapp.entity.projection.UserListItemProjection;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Page<UserListItemProjection> findListItemsBy(Pageable pageable);

    Page<UserListItemProjection> findListItemsByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
