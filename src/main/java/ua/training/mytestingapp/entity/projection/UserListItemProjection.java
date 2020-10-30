package ua.training.mytestingapp.entity.projection;

import java.time.LocalDate;

public interface UserListItemProjection {

    String getUsername();

    String getDisplayName();

    LocalDate getRegistrationDate();
}
