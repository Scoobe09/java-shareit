package ru.practicum.shareit.user;

import java.util.List;

public interface UserDao {
    User createUser(User user);

    User updateUser(User user);

    void deleteById(Integer id);

    User findById(Integer id);

    List<User> findAllUsers();

    void deleteEmail(String email);

    Integer existsByEmail(String email);

    boolean existsById(Integer id);
}
