package ru.practicum.shareit.booking;

import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "itemId", "bookerId", "status"})
public class BookingDTO {

    private Long id;

    @Future(message = "The start time is not valid")
    @NotNull(message = "Start cannot be null")
    private LocalDateTime start;

    @Future(message = "The future time is not valid")
    @NotNull(message = "End cannot be null")
    private LocalDateTime end;

    private Long itemId;

    private Long bookerId;

    private BookingState status;
}