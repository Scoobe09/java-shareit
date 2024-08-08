package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.BookingState.APPROVED;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookingRepositoryTest {
    @Mock
    private TestEntityManager testEntityManager;
    @Mock
    private BookingRepository bookingRepository;
    private User testUser;
    private Item item1;
    private Item item2;
    private Request request;
    private static final Pageable PAGEABLE = PageRequest.of(0, 20);
    private Booking booking;
    private LocalDateTime now;

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
        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(8))
                .end(LocalDateTime.now().plusDays(10))
                .status(APPROVED)
                .item(item1)
                .booker(testUser)
                .build();
        now = LocalDateTime.now();
    }

    @Test
    void updateBookingsByStatus() {
        when(bookingRepository.updateBookingsByStatus(APPROVED.toString(), booking.getId())).thenReturn(1);
        testEntityManager.persist(testUser);
        testEntityManager.persist(request);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        testEntityManager.persist(booking);
        testEntityManager.flush();
        int updated = bookingRepository.updateBookingsByStatus(APPROVED.toString(), booking.getId());
        assertEquals(1, updated);
    }

    @Test
    void existsByBookerIdAndItem_IdAndStatusInAndEndBefore() {

        LocalDateTime now = LocalDateTime.now();
        List<BookingState> states = Arrays.asList(BookingState.APPROVED, BookingState.CANCELED);
        when(bookingRepository.existsByBookerIdAndItem_IdAndStatusInAndEndBefore(testUser.getId(), item1.getId(), states, now))
                .thenReturn(true);
        testEntityManager.persist(testUser);
        testEntityManager.persist(request);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        testEntityManager.persist(booking);

        testEntityManager.flush();
        boolean exists = bookingRepository.existsByBookerIdAndItem_IdAndStatusInAndEndBefore(testUser.getId(), item1.getId(), states, now);
        assertTrue(exists);
    }


    @Test
    void getBookingByIdAndBooker_IdOrIdAndItem_Owner_Id() {
        when(bookingRepository.getBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(booking.getId(), testUser.getId(), booking.getId(), testUser.getId()))
                .thenReturn(Optional.of(booking));
        testEntityManager.persist(testUser);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        testEntityManager.persist(booking);
        testEntityManager.flush();
        Optional<Booking> foundBooking = bookingRepository.getBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(booking.getId(), testUser.getId(), booking.getId(), testUser.getId());
        assertTrue(foundBooking.isPresent());
        assertEquals(foundBooking.get(), booking);
    }

    @Test
    void findByBooker_IdAndStartIsBeforeAndEndIsAfter() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = Arrays.asList(booking, Booking.builder()
                .id(2L)
                .start(now.minusDays(2))
                .end(now.plusDays(2))
                .status(APPROVED)
                .item(item2)
                .booker(testUser)
                .build());
        when(bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(testUser.getId(), now, now, PAGEABLE))
                .thenReturn(bookings);
        testEntityManager.persist(testUser);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        bookings.forEach(testEntityManager::persist);
        testEntityManager.flush();
        List<Booking> fetchedBookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(testUser.getId(), now, now, PAGEABLE);
        assertEquals(bookings.size(), fetchedBookings.size());
        assertTrue(fetchedBookings.containsAll(bookings));
        assertTrue(bookings.containsAll(fetchedBookings));
    }


    @Test
    void getBookingByItem_Owner_Id() {
        List<Booking> bookings = List.of(booking);
        when(bookingRepository.getBookingByItem_Owner_Id(booking.getItem().getOwner().getId(), PAGEABLE)).thenReturn(bookings);
        testEntityManager.persist(testUser);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        bookings.forEach(testEntityManager::persist);
        testEntityManager.flush();

        List<Booking> fetchedBookings = bookingRepository.getBookingByItem_Owner_Id(testUser.getId(), PAGEABLE);
        assertEquals(bookings.size(), fetchedBookings.size());
    }

    @Test
    void findBookingByItem_Owner_IdAndEndIsBefore() {
        booking.setEnd(LocalDateTime.now().minusDays(2));

        List<Booking> bookings = List.of(booking);
        when(bookingRepository.findBookingByItem_Owner_IdAndEndIsBefore(testUser.getId(), now, PAGEABLE))
                .thenReturn(bookings);
        testEntityManager.persist(testUser);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        bookings.forEach(testEntityManager::persist);
        testEntityManager.flush();
        List<Booking> fetchedBookings = bookingRepository.findBookingByItem_Owner_IdAndEndIsBefore(testUser.getId(), now, PAGEABLE);
        assertEquals(bookings.size(), fetchedBookings.size());
        assertTrue(fetchedBookings.containsAll(bookings));
        assertTrue(bookings.containsAll(fetchedBookings));
    }

    @Test
    void findBookingByItem_Owner_IdAndStartIsAfter() {
        booking.setStart(LocalDateTime.now().minusDays(2));
        List<Booking> bookings = List.of(booking);
        when(bookingRepository.findBookingByItem_Owner_IdAndStartIsAfter(testUser.getId(), now, PAGEABLE))
                .thenReturn(bookings);
        testEntityManager.persist(testUser);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        bookings.forEach(testEntityManager::persist);
        testEntityManager.flush();
        List<Booking> fetchedBookings = bookingRepository.findBookingByItem_Owner_IdAndStartIsAfter(testUser.getId(), now, PAGEABLE);
        assertEquals(bookings.size(), fetchedBookings.size());
        assertTrue(fetchedBookings.containsAll(bookings));
        assertTrue(bookings.containsAll(fetchedBookings));
    }

    @Test
    void findBookingByItem_Owner_IdAndStartIsBeforeAndEndIsAfter() {
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        List<Booking> bookings = List.of(booking);
        when(bookingRepository.findBookingByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(testUser.getId(), now, now,PAGEABLE))
                .thenReturn(bookings);
        testEntityManager.persist(testUser);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        bookings.forEach(testEntityManager::persist);
        testEntityManager.flush();
        List<Booking> fetchedBookings = bookingRepository.findBookingByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(testUser.getId(), now, now, PAGEABLE);
        assertEquals(bookings.size(), fetchedBookings.size());
        assertTrue(fetchedBookings.containsAll(bookings));
        assertTrue(bookings.containsAll(fetchedBookings));
    }

    @Test
    void findByBooker_IdAndEndIsBefore() {
        List<Booking> bookings = Arrays.asList(booking, Booking.builder()
                .id(2L)
                .start(now.minusDays(2))
                .end(now.plusDays(2))
                .status(APPROVED)
                .item(item2)
                .booker(testUser)
                .build());
        when(bookingRepository.findByBooker_IdAndEndIsBefore(testUser.getId(), now, 0, 20))
                .thenReturn(bookings);
        testEntityManager.persist(testUser);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        bookings.forEach(testEntityManager::persist);
        testEntityManager.flush();
        List<Booking> fetchedBookings = bookingRepository.findByBooker_IdAndEndIsBefore(testUser.getId(), now, 0, 20);
        assertEquals(bookings.size(), fetchedBookings.size());
        assertTrue(fetchedBookings.containsAll(bookings));
        assertTrue(bookings.containsAll(fetchedBookings));
    }

    @Test
    void findByBooker_IdAndStartIsAfter() {
        List<Booking> bookings = Arrays.asList(booking, Booking.builder()
                .id(2L)
                .start(now.minusDays(2))
                .end(now.plusDays(2))
                .status(APPROVED)
                .item(item2)
                .booker(testUser)
                .build());
        when(bookingRepository.findBookingByItem_Owner_IdAndStartIsAfter(testUser.getId(), now, PAGEABLE))
                .thenReturn(bookings);
        testEntityManager.persist(testUser);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        bookings.forEach(testEntityManager::persist);
        testEntityManager.flush();
        List<Booking> fetchedBookings = bookingRepository.findBookingByItem_Owner_IdAndStartIsAfter(testUser.getId(), now, PAGEABLE);
        assertEquals(bookings.size(), fetchedBookings.size());
        assertTrue(fetchedBookings.containsAll(bookings));
        assertTrue(bookings.containsAll(fetchedBookings));
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {
        List<Booking> bookings = Arrays.asList(booking, Booking.builder()
                .id(2L)
                .start(now.minusDays(2))
                .end(now.plusDays(2))
                .status(APPROVED)
                .item(item2)
                .booker(testUser)
                .build());
        when(bookingRepository.findBookingByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(testUser.getId(), now, now, PAGEABLE))
                .thenReturn(bookings);
        testEntityManager.persist(testUser);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        bookings.forEach(testEntityManager::persist);
        testEntityManager.flush();
        List<Booking> fetchedBookings = bookingRepository.findBookingByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(testUser.getId(), now, now, PAGEABLE);
        assertEquals(bookings.size(), fetchedBookings.size());
        assertTrue(fetchedBookings.containsAll(bookings));
        assertTrue(bookings.containsAll(fetchedBookings));
    }

    @Test
    void findByItem_Owner_IdAndStatusOrderByStartDesc_Test() {
        List<Booking> bookings = Arrays.asList(booking, Booking.builder()
                .id(2L)
                .start(now.minusDays(2))
                .end(now.plusDays(2))
                .status(APPROVED)
                .item(item2)
                .booker(testUser)
                .build());
        when(bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(testUser.getId(), APPROVED, PAGEABLE))
                .thenReturn(bookings);
        testEntityManager.persist(testUser);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        bookings.forEach(testEntityManager::persist);
        testEntityManager.flush();
        List<Booking> fetchedBookings = bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(testUser.getId(), APPROVED, PAGEABLE);
        assertEquals(bookings.size(), fetchedBookings.size());
        assertTrue(fetchedBookings.containsAll(bookings));
        assertTrue(bookings.containsAll(fetchedBookings));
    }


    @Test
    void findFirstByItem_IdAndStartIsBeforeAndStatusIsNotOrderByStartDesc_Test() {

    }

    @Test
    void findFirstByItem_IdAndStartIsAfterAndStatusIsNotOrderByStartAsc_Test() {
    }

    @Test
    void findAllByItemInAndStatusOrderByStartAsc_Test() {
    }

    @Test
    void findBookingByStatusOrderByIdDesc_Test() {
    }
}