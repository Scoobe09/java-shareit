package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    public static final HashMap<Integer, User> users = new HashMap<>();
    public static final HashMap<String, Integer> emails = new HashMap<>();
    private Integer id1 = 0;


    @Override
    public User createUser(User user) {
        user.setId(++id1);
        users.put(user.getId(), user);
        emails.put(user.getEmail(), user.getId());

        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        emails.put(user.getEmail(), user.getId());
        return user;
    }

    @Override
    public void deleteById(Integer id) {
        User user = users.remove(id);
        emails.remove(user.getEmail());
    }

    @Override
    public User findById(Integer id) {
        return users.get(id);
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteEmail(String email) {
        emails.remove(email);
    }

    @Override
    public Integer existsByEmail(String email) {
        return emails.get(email);
    }

    @Override
    public boolean existsById(Integer id) {
        return users.containsKey(id);
    }
}
