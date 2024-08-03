package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.ItemDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "items"})
public class RequestDTOResponse {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDTO> items;
}
