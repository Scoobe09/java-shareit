package ru.practicum.shareit.item;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
public class CommentDTO {
    private Long id;

    @NotNull(message = "Text cannot be null")
    @NotEmpty(message = "Text cannot be empty")
    private String text;

    private Long authorId;
    private String authorName;
    private Long itemId;
    private LocalDateTime created;
}
