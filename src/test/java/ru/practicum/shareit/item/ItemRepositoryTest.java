package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ItemRepositoryTest {

    @Mock
    private TestEntityManager testEntityManager;

    @Mock
    private ItemRepository itemRepository;

    private User testUser;

    private Item item1;

    private Item item2;

    private Request request;

    private static final Pageable PAGEABLE = PageRequest.of(0, 20);

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("scoobe")
                .email("scoobe@mail.com")
                .build();
        request = Request.builder()
                .id(1L)
                .requestor(testUser)
                .description("request")
                .created(LocalDateTime.now())
                .build();
        item1 = Item.builder()
                .id(1L)
                .name("Armature")
                .description("Armature to beat the evil neighbors")
                .available(true)
                .owner(testUser)
                .requestId(1L)
                .build();
        item2 = Item.builder()
                .id(2L)
                .name("Armature")
                .description("Armature to beat the evil neighbors")
                .available(true)
                .owner(testUser)
                .requestId(1L)
                .build();
    }

    @Test
    void findAllByOwnerIdOrderByIdTest() {

        when(itemRepository.findAllByOwnerIdOrderById(any(Long.class), any(Pageable.class))).thenReturn(Arrays.asList(item1, item2));

        testEntityManager.persist(testUser);
        testEntityManager.persist(request);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        testEntityManager.flush();


        List<Item> foundItemsByOwner = itemRepository.findAllByOwnerIdOrderById(testUser.getId(), PAGEABLE);

        assertThat(foundItemsByOwner)
                .isNotNull()
                .hasSize(2)
                .containsExactly(item1, item2);
    }

    @DisplayName("Поиск по имени или описанию")
    @Test
    void findByNameOrDescriptionTest() {
        String armature = "Armature";
        when(itemRepository.findByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(
                any(String.class), any(String.class), any(Pageable.class))).thenReturn(Arrays.asList(item1, item2));

        testEntityManager.persist(testUser);
        testEntityManager.persist(request);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        testEntityManager.flush();
        List<Item> items = itemRepository.findByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(
                armature, armature, PAGEABLE);

        assertThat(items)
                .isNotNull()
                .hasSize(2)
                .containsExactly(item1, item2);
    }

    @Test
    void findAllByRequestIdTest() {
        when(itemRepository.findAllByRequestId(any(Long.class), any(Pageable.class))).thenReturn(Arrays.asList(item1, item2));
        testEntityManager.persist(testUser);
        testEntityManager.persist(request);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        testEntityManager.flush();
        List<Item> items = itemRepository.findAllByRequestId(request.getId(), PAGEABLE);
        assertThat(items)
                .isNotNull()
                .hasSize(2)
                .containsExactly(item1, item2);
    }

    @Test
    void findAllByOwner_IdAndRequestIdTest() {
        when(itemRepository.findAllByOwner_IdAndRequestId(any(Long.class), any(Long.class))).thenReturn(Arrays.asList(item1, item2));
        testEntityManager.persist(testUser);
        testEntityManager.persist(request);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        testEntityManager.flush();
        List<Item> items = itemRepository.findAllByOwner_IdAndRequestId(testUser.getId(), request.getId());
        assertThat(items)
                .isNotNull()
                .hasSize(2)
                .containsExactly(item1, item2);
    }
}