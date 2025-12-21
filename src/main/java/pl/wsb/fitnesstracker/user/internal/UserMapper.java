package pl.wsb.fitnesstracker.user.internal;

import org.springframework.stereotype.Component;
import pl.wsb.fitnesstracker.user.api.*;

@Component
class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthdate(),
                user.getEmail());
    }

    public UserSimpleDto toSimpleDto(User user) {
        return new UserSimpleDto(user.getId(),
                user.getFirstName(),
                user.getLastName());
    }

    public User toEntity(UserRequestDto dto) {
        return new User(dto.firstName(), dto.lastName(), dto.birthdate(), dto.email());
    }

    public UserIdEmailDto toUserIdEmailDto(User user) {
        return new UserIdEmailDto(user.getId(), user.getEmail());
    }
}
