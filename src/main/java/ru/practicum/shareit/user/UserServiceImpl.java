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
        checkEmail(userDto);

        return mapper.toDTO(userDao.createUser(mapper.toModel(userDto)));
    }

    @Override
    public UserDto updateUser(Integer id, UserDto userDto) {

        log.info("Обновление пользователя с идентификатором: {}", id);

        UserDto userDtoFromDB = mapper.toDTO(userDao.findById(id));
        if (userDtoFromDB == null) {
            throw new InvalidIdException("Нмбыз", HttpStatus.NOT_FOUND);
        }
        userDto.setId(id);
        if (userDto.getName() == null) {
            userDto.setName(userDtoFromDB.getName());
        }

        if (userDto.getEmail() == null) {
            userDto.setEmail(userDtoFromDB.getEmail());
        }

        checkEmailInDB(userDto);
        userDao.deleteEmail(userDtoFromDB.getEmail());

        log.info("Пользователь с идентификатором: {} успешно обновлен", userDto.getId());
        return mapper.toDTO(userDao.updateUser(mapper.toModel(userDto)));
    }

    @Override
    public void deleteById(Integer id) {

        log.info("Удаление пользователя с идентификатором: {}", id);

        if (!userDao.existsById(id)) {
            throw new InvalidIdException("Не удалось найти пользователя", HttpStatus.NOT_FOUND);
        }
        log.info("Пользователь с идентификатором: {} успешно удален.", id);
        userDao.deleteById(id);
    }

    @Override
    public UserDto findById(Integer id) {

        log.info("Получение пользователя по идентификатору: {}", id);

        if (userDao.existsById(id)) {
            log.info("Пользователь получен с идентификатором: {}", id);
            return mapper.toDTO(userDao.findById(id));
        }
        throw new InvalidIdException("Не удалось найти пользователя", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<UserDto> findAllUsers() {

        log.info("Получение всех пользователей");
        log.info("Пользователи получены");

        return mapper.toDtoList(userDao.findAllUsers());
    }

    @Override
    public boolean existsById(Integer id) {
        log.info("Проверка существования пользователя с идентификатором: {}. Существует: {}", id, userDao.existsById(id));
        return userDao.existsById(id);
    }

    private void checkEmail(UserDto userDto) {

        if (userDao.existsByEmail(userDto.getEmail()) != null) {
            throw new InvalidIdException("Емайл уже существует", HttpStatus.CONFLICT);
        }
    }

    private void checkEmailInDB(UserDto userDto) {

        Integer id1 = userDao.existsByEmail(userDto.getEmail());
        if (id1 != null) {
            if (!id1.equals(userDto.getId())) {
                throw new InvalidIdException("Емайл уже существует", HttpStatus.CONFLICT);
            }
        }
    }
}
