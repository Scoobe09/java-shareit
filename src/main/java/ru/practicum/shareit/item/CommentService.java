package ru.practicum.shareit.item;

public interface CommentService {
    CommentDTO addComment(final Long userId, final Long itemId, CommentDTO commentDTO);
}