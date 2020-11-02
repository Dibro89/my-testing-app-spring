package ua.training.mytestingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.training.mytestingapp.entity.Test;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findAllByOrderByName();

    List<Test> findAllBySubjectOrderByName(String subject);

    List<Test> findAllByOrderByDifficulty();

    List<Test> findAllBySubjectOrderByDifficulty(String subject);

    @Query("select t from Test t order by size(t.attempts)")
    List<Test> findAllByOrderByAttempts();

    @Query("select t from Test t where t.subject = ?1 order by size(t.attempts)")
    List<Test> findAllBySubjectOrderByAttempts(String subject);

    @Query("select distinct t.subject from Test t")
    List<String> findAllSubjects();
}
