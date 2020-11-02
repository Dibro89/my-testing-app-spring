package ua.training.mytestingapp.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserEditForm {

    @NotNull
    @Size(min = 6)
    private String password;

    @NotNull
    @Size(min = 2)
    private String displayName;

    private boolean admin;
}
