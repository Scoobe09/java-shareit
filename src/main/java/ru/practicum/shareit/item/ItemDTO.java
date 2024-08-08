package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.booking.BookingShortDTO;
import ru.practicum.shareit.user.UserDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.utils.Marker.OnCreate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "owner", "requestId"})
public class ItemDTO {

    private Long id;

    @NotBlank(message = "Name cannot be blank", groups = OnCreate.class)
    private String name;

    @NotBlank(message = "Description cannot be blank", groups = OnCreate.class)
    private String description;

    @NotNull(message = "Available cannot be null", groups = OnCreate.class)
    private Boolean available;

    private UserDTO owner;

    private BookingShortDTO lastBooking;

    private BookingShortDTO nextBooking;

    private List<CommentDTO> comments;

    private LocalDateTime created = LocalDateTime.now();

    private Long requestId;
}