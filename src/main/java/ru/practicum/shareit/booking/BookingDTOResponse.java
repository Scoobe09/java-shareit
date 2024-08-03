package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.item.ItemDTO;
import ru.practicum.shareit.user.UserDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "item", "booker", "status"})
public class BookingDTOResponse {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDTO item;
    private UserDTO booker;
    private BookingState status;
}
