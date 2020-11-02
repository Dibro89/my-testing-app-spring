package ua.training.mytestingapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AttemptForm {

    private Long userId;
    private Long testId;

    private Map<String, String[]> parameterMap;
}
