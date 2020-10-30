package ua.training.mytestingapp.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.training.mytestingapp.entity.Test;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findAllBySubject(String subject);

    List<Test> findAllBySubject(String subject, Sort sort);

    @Query("select distinct t.subject from Test t")
    List<String> findAllSubjects();
}
