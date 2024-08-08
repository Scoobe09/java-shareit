package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.CommentDTO;
import ru.practicum.shareit.item.CommentMapperImpl;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CommentMapperImplTest {

    private final CommentMapperImpl commentMapper = new CommentMapperImpl();

    @Test
    void toDTO_whenCommentIsNull() {
        Comment comment = null;
        CommentDTO result = commentMapper.toDTO(comment);
        assertNull(result);
    }

    @Test
    void toDTO_whenCommentIsNotNull() {
        LocalDateTime time = LocalDateTime.now();

        User user = User.builder().id(1L).name("username").build();
        Comment comment = Comment.builder()
                .id(1L)
                .text("text")
                .author(user)
                .created(time)
                .build();

        CommentDTO result = commentMapper.toDTO(comment);

        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getText(), result.getText());
        assertEquals(comment.getAuthor().getId(), result.getAuthorId());
        assertEquals(comment.getAuthor().getName(), result.getAuthorName());
        assertEquals(comment.getCreated(), result.getCreated());
    }

    @Test
    void toDTO_whenCommentHasNullAuthor() {
        LocalDateTime time = LocalDateTime.now();

        Comment comment = Comment.builder()
                .id(1L)
                .text("text")
                .author(null)
                .created(time)
                .build();

        CommentDTO result = commentMapper.toDTO(comment);

        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getText(), result.getText());
        assertNull(result.getAuthorId());
        assertNull(result.getAuthorName());
        assertEquals(comment.getCreated(), result.getCreated());
    }

    @Test
    void toModel_whenCommentDTOIsNull() {
        CommentDTO commentDTO = null;
        Comment result = commentMapper.toModel(commentDTO);
        assertNull(result);
    }

    @Test
    void toModel_whenCommentDTOIsNotNull() {
        LocalDateTime time = LocalDateTime.now();

        CommentDTO commentDTO = CommentDTO.builder()
                .id(1L)
                .text("text")
                .authorId(1L)
                .authorName("username")
                .created(time)
                .build();

        Comment result = commentMapper.toModel(commentDTO);

        assertEquals(commentDTO.getId(), result.getId());
        assertEquals(commentDTO.getText(), result.getText());
        assertEquals(commentDTO.getAuthorId(), result.getAuthor().getId());
        assertEquals(commentDTO.getAuthorName(), result.getAuthor().getName());
        assertEquals(commentDTO.getCreated(), result.getCreated());
    }

    @Test
    void toModel_whenCommentDTOHasNullAuthorIdAndName() {
        LocalDateTime time = LocalDateTime.now();

        CommentDTO commentDTO = CommentDTO.builder()
                .id(1L)
                .text("text")
                .authorId(null)
                .authorName(null)
                .created(time)
                .build();

        Comment result = commentMapper.toModel(commentDTO);

        assertEquals(commentDTO.getText(), result.getText());

        assertEquals(commentDTO.getCreated(), result.getCreated());
    }

    @Test
    void toDTOList_whenCommentsIsNull() {
        List<Comment> comments = null;
        List<CommentDTO> result = commentMapper.toDTOList(comments);
        assertNull(result);
    }

    @Test
    void toDTOList_whenCommentsIsNotNull() {
        LocalDateTime time = LocalDateTime.now();
        User user = User.builder().id(1L).name("username").build();
        Comment comment1 = Comment.builder()
                .id(1L)
                .text("text")
                .author(user)
                .created(time)
                .build();
        Comment comment2 = Comment.builder()
                .id(2L)
                .text("another text")
                .author(user)
                .created(time)
                .build();
        List<Comment> comments = List.of(comment1, comment2);
        List<CommentDTO> result = commentMapper.toDTOList(comments);
        assertEquals(comments.size(), result.size());
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            CommentDTO dto = result.get(i);
            assertEquals(comment.getId(), dto.getId());
            assertEquals(comment.getText(), dto.getText());
            assertEquals(comment.getAuthor().getId(), dto.getAuthorId());
            assertEquals(comment.getAuthor().getName(), dto.getAuthorName());
            assertEquals(comment.getCreated(), dto.getCreated());
        }
    }

    @Test
    void commentDTOToUser_whenCommentDTOIsNull() {
        CommentDTO commentDTO = null;
        User result = commentMapper.commentDTOToUser(commentDTO);
        assertNull(result);
    }

    @Test
    void commentDTOToUser_whenCommentDTOIsNotNull() {
        CommentDTO commentDTO = CommentDTO.builder()
                .authorId(1L)
                .authorName("username")
                .build();

        User result = commentMapper.commentDTOToUser(commentDTO);

        assertEquals(commentDTO.getAuthorId(), result.getId());
        assertEquals(commentDTO.getAuthorName(), result.getName());
    }

    @Test
    void commentDTOToUser_whenCommentDTOHasNullAuthorIdAndName() {
        CommentDTO commentDTO = CommentDTO.builder()
                .authorId(null)
                .authorName(null)
                .build();

        User result = commentMapper.commentDTOToUser(commentDTO);

        assertNull(result.getId());
        assertNull(result.getName());
    }



}
