package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Integer id, ItemDto itemDto);

    ItemDto updateItem(Integer id1, Integer id2, ItemDto itemDto);

    ItemDto getItemById(Integer id);

    List<ItemDto> getAllItems(Integer userId);

    List<ItemDto> getItemsByNameOrDescription(String text);
}
