package ua.training.mytestingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.training.mytestingapp.entity.Attempt;

import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    List<Attempt> findAllByUser_Id(Long userId);
}
