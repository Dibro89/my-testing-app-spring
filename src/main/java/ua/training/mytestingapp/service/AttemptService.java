package ua.training.mytestingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.training.mytestingapp.entity.Attempt;
import ua.training.mytestingapp.entity.User;
import ua.training.mytestingapp.repository.AttemptRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttemptService {

    private final AttemptRepository attemptRepository;

    public Attempt save(Attempt attempt) {
        return attemptRepository.save(attempt);
    }

    public List<Attempt> findByUser(User user) {
        return attemptRepository.findAllByUser_Id(user.getId());
    }
}
