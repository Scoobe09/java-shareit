package ru.practicum.shareit.item;

import java.util.List;

public interface ItemDao {
    Item createItem(Item item);

    Item updateItem(Item item);

    Item getItemById(Integer id);

    List<Item> findAllByOwnerId(Integer userId);

    List<Item> findAllByNameOrDescription(String text);
}
