package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.InvalidIdException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserMapper mapper;


    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Добавление нового пользователя с адресом электронной почты: {}", userDto.getEmail());

        return mapper.toDTO(userDao.createUser(mapper.toModel(userDto)));
    }

    @Override
    public UserDto updateUser(Integer id, UserDto userDto) {

        log.info("Обновление пользователя с идентификатором: {}", id);

        UserDto userDtoFromDB = mapper.toDTO(userDao.findById(id));
        if (userDtoFromDB == null) {
            throw new InvalidIdException("Такого пользователя не существует", HttpStatus.NOT_FOUND);
        }
        userDto.setId(id);
        if (userDto.getName() == null) {
            userDto.setName(userDtoFromDB.getName());
        }

        if (userDto.getEmail() == null) {
            userDto.setEmail(userDtoFromDB.getEmail());
        }

        userDao.deleteEmail(userDtoFromDB.getEmail());

        UserDto newUser = mapper.toDTO(userDao.updateUser(mapper.toModel(userDto)));

        log.info("Пользователь с идентификатором: {} успешно обновлен", userDto.getId());
        return newUser;
    }

    @Override
    public void deleteById(Integer id) {

        log.info("Удаление пользователя с идентификатором: {}", id);

        if (!userDao.existsById(id)) {
            throw new InvalidIdException("Не удалось найти пользователя", HttpStatus.NOT_FOUND);
        }
        userDao.deleteById(id);
        log.info("Пользователь с идентификатором: {} успешно удален.", id);
    }

    @Override
    public UserDto findById(Integer id) {

        log.info("Получение пользователя по идентификатору: {}", id);

        if (id == null) {
            throw new InvalidIdException("Не удалось найти пользователя", HttpStatus.NOT_FOUND);
        }
        UserDto newUser = mapper.toDTO(userDao.findById(id));
        log.info("Пользователь получен с идентификатором: {}", id);
        return newUser;
    }

    @Override
    public List<UserDto> findAllUsers() {

        log.info("Получение всех пользователей");

        List<UserDto> newList = mapper.toDtoList(userDao.findAllUsers());
        log.info("Пользователи получены");

        return newList;
    }

    @Override
    public boolean existsById(Integer id) {
        log.info("Проверка существования пользователя с идентификатором: {}. Существует: {}", id, userDao.existsById(id));
        return userDao.existsById(id);
    }
}
