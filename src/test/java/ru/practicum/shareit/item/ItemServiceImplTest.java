package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapperImpl;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.BookingState.APPROVED;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private User createOwner;
    private Item createItem;
    @Autowired
    private UserMapperImpl userMapperImpl;


    @BeforeEach
    void beforeEach() {
        createOwner = User.builder()
                .name("John")
                .email("some@email.com")
                .build();
        userRepository.save(createOwner);

        createItem = Item.builder()
                .name("Аккумуляторная дрель")
                .description("Аккумуляторная дрель")
                .available(true)
                .owner(createOwner)
                .build();
        itemRepository.save(createItem);
    }

    @Test
    void createItem() {
        final ItemDTO itemDto = ItemDTO.builder()
                .name("Дрель")
                .description("Дрель")
                .available(true)
                .build();

        final ItemDTO resItemDto = itemService.addItem(createOwner.getId(), itemDto);
        final Item resItem = itemRepository.findById(resItemDto.getId()).get();

        assertThat(resItem.getId(), equalTo(resItemDto.getId()));
        assertThat(resItem.getName(), equalTo(resItemDto.getName()));
        assertThat(resItem.getDescription(), equalTo(resItemDto.getDescription()));
        assertThat(resItem.getAvailable(), equalTo(resItemDto.getAvailable()));
        assertThat(resItem.getOwner(), equalTo(createOwner));
    }

    @Test
    void createItemWithWrongUser() {
        final ItemDTO itemDto = ItemDTO.builder()
                .name("Дрель")
                .description("Дрель")
                .available(true)
                .build();
        try {
            itemService.addItem(100L, itemDto);
        } catch (NotFoundException e) {
            AssertionsForClassTypes.assertThat(e).isInstanceOf(NotFoundException.class);
        }
    }

    @Test
    void updateItem() {
        final ItemDTO itemDto = ItemDTO.builder()
                .name("Updated Drill")
                .description("Updated Drill")
                .available(false)
                .build();

        final ItemDTO resItemDto = itemService.updateItem(createOwner.getId(), createItem.getId(), itemDto);
        final Item resItem = itemRepository.findById(resItemDto.getId()).get();

        assertThat(resItem.getId(), equalTo(resItemDto.getId()));
        assertThat(resItem.getName(), equalTo(resItemDto.getName()));
        assertThat(resItem.getDescription(), equalTo(resItemDto.getDescription()));
        assertThat(resItem.getAvailable(), equalTo(resItemDto.getAvailable()));
        assertThat(resItem.getOwner(), equalTo(createOwner));
    }

    @Test
    void updateItemWithWrongId() {
        final ItemDTO itemDto = ItemDTO.builder()
                .name("Updated Drill")
                .description("Updated Drill")
                .available(false)
                .build();
        try {
            itemService.updateItem(createOwner.getId(), 100L, itemDto);
        } catch (NotFoundException e) {
            AssertionsForClassTypes.assertThat(e).isInstanceOf(NotFoundException.class);
        }
    }

    @Test
    void findItemById() {
        final ItemDTO itemDto = ItemDTO.builder()
                .name("Дрель")
                .description("Дрель")
                .available(true)
                .build();

        final ItemDTO resItemDto = itemService.addItem(createOwner.getId(), itemDto);
        final ItemDTO itemDTO = itemService.findItemById(resItemDto.getId(), createOwner.getId());

        assertThat(itemDTO.getId(), equalTo(resItemDto.getId()));
        assertThat(itemDTO.getName(), equalTo(resItemDto.getName()));
        assertThat(itemDTO.getDescription(), equalTo(resItemDto.getDescription()));
        assertThat(itemDTO.getAvailable(), equalTo(resItemDto.getAvailable()));
        assertThat(itemDTO.getOwner(), equalTo(userMapperImpl.toDTO(createOwner)));
    }

    @Test
    void findItemByWrongUserId() {
        final ItemDTO itemDto = ItemDTO.builder()
                .name("Дрель")
                .description("Дрель")
                .available(true)
                .build();
        try {
            itemService.addItem(createOwner.getId(), itemDto);
            itemService.findItemById(100L, createOwner.getId());
        } catch (NotFoundException e) {
            AssertionsForClassTypes.assertThat(e).isInstanceOf(NotFoundException.class);
        }
    }

    @Test
    void getAllItems() {
        ItemDTO itemDto = ItemDTO.builder()
                .name("Дрель")
                .description("Дрель")
                .available(true)
                .build();
        ItemDTO itemDto1 = ItemDTO.builder()
                .name("Дрель")
                .description("Дрель")
                .available(true)
                .build();

        List<ItemDTO> items = itemService.getItems(createOwner.getId(), 0, 20);
        if (items.isEmpty()) {
            fail("No items found for testing");
        }
        List<ItemDTO> itemDTOs = itemService.getItems(createOwner.getId(), 0, 20);
        assertThat(itemDTOs.size(), equalTo(items.size()));
        for (ItemDTO itemDTO : itemDTOs) {
            assertNotNull(itemDTO.getName());
            assertNotNull(itemDTO.getDescription());
            assertNotNull(itemDTO.getAvailable());
        }
    }

    @Test
    void getItemsByNameOrDescriptionTest() {
        String string = "Аккум";
        List<Item> items = itemRepository.findByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(string, string, PageRequest.of(0, 20));
        if (items.isEmpty()) {
            fail("No items found for testing");
        }
        List<ItemDTO> itemDTOs = itemService.getItemsByNameOrDescription(string, 0, 20);
        assertThat(itemDTOs.size(), equalTo(items.size()));
        for (ItemDTO itemDTO : itemDTOs) {
            assertNotNull(itemDTO.getName());
            assertNotNull(itemDTO.getDescription());
            assertNotNull(itemDTO.getAvailable());
        }
    }

    @Test
    void getItemsByEmptyNameOrDescriptionTest() {
        String string = "Аккум";
        List<Item> items = itemRepository.findByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(string, string, PageRequest.of(0, 20));
        if (items.isEmpty()) {
            fail("No items found for testing");
        }

        List<ItemDTO> itemDTOs = itemService.getItemsByNameOrDescription(null, 0, 20);
        assertEquals(itemDTOs, Collections.emptyList());
    }

    @Test
    void addComment() {
        final User author = User.builder()
                .name("John")
                .email("hfkg@email.com")
                .build();
        userRepository.save(author);
        final Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().minusDays(1))
                .item(createItem)
                .booker(author)
                .status(APPROVED)
                .build();
        bookingRepository.save(booking);
        final CommentDTO commentdto = CommentDTO.builder()
                .text("some comment")
                .build();

        CommentDTO commentDTO = commentService.addComment(author.getId(), createItem.getId(), commentdto);
        Comment comment = commentRepository.findById(commentDTO.getId()).get();

        assertThat(commentDTO.getId(), equalTo(comment.getId()));
        assertThat(commentDTO.getText(), equalTo(comment.getText()));

    }

    @Test
    void addEmptyComment() {
        assertThrows(BadRequestException.class, () ->
                commentService.addComment(createOwner.getId(), createItem.getId(), CommentDTO.builder().build()));
    }
}