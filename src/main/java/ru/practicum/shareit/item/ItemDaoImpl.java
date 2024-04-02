package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.InvalidIdException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ItemDaoImpl implements ItemDao {

    private final HashMap<Integer, Item> items = new HashMap<>();
    private int id1 = 0;

    @Override
    public Item createItem(Item item) {
        item.setId(++id1);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemById(Integer id) {
        if (!items.containsKey(id)) {
            throw new InvalidIdException("Нет элемента с данным ID", HttpStatus.NOT_FOUND);
        }
        return items.get(id);
    }

    @Override
    public List<Item> findAllByOwnerId(Integer userId) {
        return items.values().stream()
                .filter(z -> z.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findAllByNameOrDescription(String text) {
        List<Item> items1 = new ArrayList<>();
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text))
                    && item.getAvailable()) {
                items1.add(item);
            }
        }
        return items1;
    }
}
