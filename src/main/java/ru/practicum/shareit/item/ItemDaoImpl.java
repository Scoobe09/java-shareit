package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ItemDaoImpl implements ItemDao {

    public static final HashMap<Integer, Item> items = new HashMap<>();
    private Integer id1 = 0;

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
            if (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)
                    && item.getAvailable()) {
                items1.add(item);
            }
        }
        return items1;
    }

    @Override
    public boolean existsById(Integer id) {
        return items.containsKey(id);
    }
}
