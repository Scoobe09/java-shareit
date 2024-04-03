package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
}
