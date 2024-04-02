package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.InvalidIdException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final HashMap<Integer, User> users = new HashMap<>();
    private final HashMap<String, Integer> emails = new HashMap<>();
    private int id1 = 0;


    @Override
    public User createUser(User user) {
        checkEmail(user);
        user.setId(++id1);
        users.put(user.getId(), user);
        emails.put(user.getEmail(), user.getId());

        return user;
    }

    @Override
    public User updateUser(User user) {
        checkEmailInDB(user);
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

    private void checkEmail(User user) {

        if (existsByEmail(user.getEmail()) != null) {
            throw new InvalidIdException("Емайл уже существует", HttpStatus.CONFLICT);
        }
    }

    private void checkEmailInDB(User user) {

        Integer id1 = existsByEmail(user.getEmail());
        if (id1 != null) {
            if (!id1.equals(user.getId())) {
                throw new InvalidIdException("Емайл уже существует", HttpStatus.CONFLICT);
            }
        }
    }
}
