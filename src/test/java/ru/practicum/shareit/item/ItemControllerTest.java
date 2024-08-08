package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.ItemConstant.X_SHARER_USER_ID;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentService commentService;

    private final MockMvc mockMvc;


    private final ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void createItem_success() {
        Long userId = 1L;
        ItemDTO itemDTO = ItemDTO.builder()
                .name("Book")
                .description("Interesting book")
                .available(true)
                .build();
        ItemDTO responseDto = ItemDTO.builder()
                .name("Book")
                .description("Interesting book")
                .available(true)
                .build();

        when(itemService.addItem(eq(userId), any(ItemDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO))
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    void updateItem_success() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDTO itemDTO = ItemDTO.builder()
                .name("Book")
                .description("Interesting book")
                .available(true)
                .build();
        ItemDTO responseDto = ItemDTO.builder()
                .name("Book")
                .description("Interesting book")
                .available(true)
                .build();

        when(itemService.updateItem(eq(userId), eq(itemId), any(ItemDTO.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO))
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getItem_success() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDTO responseDto = ItemDTO.builder()
                .name("Book")
                .description("Interesting book")
                .available(true)
                .build();
        when(itemService.findItemById(eq(itemId), eq(userId))).thenReturn(responseDto);
        mockMvc.perform(get("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    @SneakyThrows
    void getAllItems_success() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        List<ItemDTO> responseDtoList = Arrays.asList(
                ItemDTO.builder()
                        .name("Book")
                        .description("Interesting book")
                        .available(true)
                        .build(),
                ItemDTO.builder()
                        .name("Pen")
                        .description("Blue pen")
                        .available(true)
                        .build()
        );

        when(itemService.getItems(eq(userId), eq(from), eq(size))).thenReturn(responseDtoList);

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDtoList)));
    }

    @Test
    @SneakyThrows
    void searchByTextSuccess() {
        Long userId = 1L;
        String text = "book";
        Integer from = 0;
        Integer size = 10;
        List<ItemDTO> responseDtoList = Arrays.asList(
                ItemDTO.builder()
                        .name("Book")
                        .description("Interesting book")
                        .available(true)
                        .build(),
                ItemDTO.builder()
                        .name("Bookshelf")
                        .description("Wooden bookshelf")
                        .available(true)
                        .build()
        );

        when(itemService.getItemsByNameOrDescription(eq(text), eq(from), eq(size))).thenReturn(responseDtoList);

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, userId)
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDtoList)));
    }

    @Test
    @SneakyThrows
    void createCommentSuccess() {
        Long userId = 1L;
        Long itemId = 1L;

        CommentDTO commentDTO = CommentDTO.builder()
                .text("Scoobe the best programmer in the world")
                .build();
        CommentDTO commentResponse = CommentDTO.builder()
                .text("Scoobe the best programmer in the world")
                .build();

        when(commentService.addComment(eq(userId), eq(itemId), any(CommentDTO.class))).thenReturn(commentResponse);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk());
    }
}