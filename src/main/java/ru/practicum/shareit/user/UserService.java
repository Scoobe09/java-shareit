package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(Integer id, UserDto userDto);

    void deleteById(Integer id);

    UserDto findById(Integer id);

    List<UserDto> findAllUsers();

    boolean existsById(Integer id);
}
