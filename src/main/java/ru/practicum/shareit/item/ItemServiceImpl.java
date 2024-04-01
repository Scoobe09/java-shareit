package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.user.UserDao;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    private final ItemMapper mapper;
    private final UserDao userDao;
    private final UserMapper userMapper;

    @Override
    public ItemDto createItem(Integer id, ItemDto itemDto) {

        log.info("Попытка добавить элемент для идентификатора пользователя: {}", id);

        UserDto userDto = userMapper.toDTO(userDao.findById(id));
        if (userDto == null) {
            throw new InvalidIdException("Пользователя не существует", HttpStatus.NOT_FOUND);
        }
        itemDto.setOwner(userDto);

        log.info("Элемент с идентификатором: {} успешно добавлен", itemDto.getId());

        return mapper.toDto(itemDao.createItem(mapper.toModel(itemDto)));
    }

    @Override
    public ItemDto updateItem(Integer ownerId, Integer itemId, ItemDto itemDto) {

        log.info("Обновление элемента с идентификатором: {} для идентификатора пользователя: {}", itemId, ownerId);

        ItemDto itemDtoFromDB = mapper.toDto(itemDao.getItemById(itemId));
        if (itemDtoFromDB == null) {
            throw new InvalidIdException("Нмбыз", HttpStatus.NOT_FOUND);
        }
        if (!itemDtoFromDB.getOwner().getId().equals(ownerId)) {
            throw new InvalidIdException("Нмбыз", HttpStatus.NOT_FOUND);
        }
        itemDto.setOwner(itemDtoFromDB.getOwner());

        if (itemDto.getName() == null) {
            itemDto.setName(itemDtoFromDB.getName());
        }

        if (itemDto.getAvailable() == null) {
            itemDto.setAvailable(itemDtoFromDB.getAvailable());
        }

        if (itemDto.getDescription() == null) {
            itemDto.setDescription(itemDtoFromDB.getDescription());
        }

        log.info("Элемент с идентификатором: {} успешно обновлен.", itemId);
        return mapper.toDto(itemDao.updateItem(mapper.toModel(itemDto)));
    }

    @Override
    public ItemDto getItemById(Integer id) {

        log.info("Получение элемента по идентификатору: {}", id);

        if (itemDao.existsById(id)) {
            log.info("Элемент получен с идентификатором: {}", id);
            return mapper.toDto(itemDao.getItemById(id));
        }
        throw new InvalidIdException("Не удалось найти предмет", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<ItemDto> getAllItems(Integer userId) {

        log.info("Получение элементов для идентификатора пользователя: {}", userId);

        isExistUser(userId);

        log.info("Полученны элементы для идентификатора пользователя: {}", userId);
        return mapper.toListDto(itemDao.findAllByOwnerId(userId));
    }

    @Override
    public List<ItemDto> getItemsByNameOrDescription(String text) {
        log.info("Поиск элементов по названию или совпадению описания: {}", text);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }

        log.info("Получены элементы по названию или совпадению описания: {}", text);
        return mapper.toListDto(itemDao.findAllByNameOrDescription(text.toLowerCase()));
    }

    private void isExistUser(Integer id) {

        if (!userDao.existsById(id)) {
            throw new InvalidIdException("Такого пользователя не существует", HttpStatus.NOT_FOUND);
        }
    }
}
