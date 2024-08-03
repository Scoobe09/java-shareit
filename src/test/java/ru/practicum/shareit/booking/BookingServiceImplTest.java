

package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnSupportedStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDTO;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDTO;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.BookingState.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceImplTest {
    @MockBean
    private BookingRepository bookingRepositoryMock;
    @MockBean
    private BookingMapper bookingMapperMock;
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ItemRepository itemRepository;
    private User user;
    private Booking booking;
    private Item item;
    private BookingDTO bookingDTO;
    private User anotherUser;
    private User secondUser;
    private BookingDTOResponse bookingDTOResponse;
    private static final int from = 0;
    private static final int size = 20;
    Sort sort = Sort.by(Sort.Direction.DESC, "start");
    Pageable pageable = PageRequest.of(from, size, sort);

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Bebra");
        user.setEmail("Bebraaawdasdwaaaaaaaaaaa@mail.com");
        userRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Bebra");
        userDTO.setEmail("Bebraaawdasdwaaaaaaaaaaa@mail.com");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setName("Bebra");
        userDTO2.setEmail("Bebraaawdasdwadaaaaaaaaaaa@mail.com");

        anotherUser = new User();
        anotherUser.setName("AnotherUser");
        anotherUser.setEmail("anadawdasdawdawdaotheruser@example.com");
        userRepository.save(anotherUser);

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(1L);
        itemDTO.setName("BookingItem");
        itemDTO.setDescription("BookingItemDescription");
        itemDTO.setAvailable(true);
        itemDTO.setOwner(userMapper.toDTO(anotherUser));
        itemDTO.setRequestId(3L);

        item = new Item();
        item.setId(1L);
        item.setName("BookingItem");
        item.setDescription("BookingItemDescription");
        item.setAvailable(true);
        item.setOwner(anotherUser);
        itemRepository.save(item);

        booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(10));
        booking.setEnd(LocalDateTime.now().plusDays(10));
        booking.setItem(item);
        booking.setBooker(user);

        bookingDTO = new BookingDTO();
        bookingDTO.setStart(LocalDateTime.now().minusDays(10));
        bookingDTO.setEnd(LocalDateTime.now().plusDays(10));
        bookingDTO.setItemId(1L);
        bookingDTO.setBookerId(1L);

        bookingDTOResponse = new BookingDTOResponse();
        bookingDTOResponse.setId(1L);
        bookingDTOResponse.setItem(itemMapper.toDTO(item));
    }

    @Test
    void addBookingTest() {
        when(bookingMapperMock.toModel(bookingDTO)).thenReturn(booking);
        when(bookingRepositoryMock.save(booking)).thenReturn(booking);
        when(bookingMapperMock.toDTO(booking)).thenReturn(bookingDTOResponse);

        BookingDTOResponse result = bookingService.addBooking(bookingDTO, user.getId());

        assertThat(result).isNotNull();
        verify(bookingRepositoryMock, times(1)).save(booking);
        verify(bookingMapperMock, times(1)).toModel(bookingDTO);
        verify(bookingMapperMock, times(1)).toDTO(booking);
    }

    @Test
    void addBookingWithWrongItemAvailable() {
        when(bookingMapperMock.toModel(bookingDTO)).thenReturn(booking);
        when(bookingRepositoryMock.save(booking)).thenReturn(booking);
        when(bookingMapperMock.toDTO(booking)).thenReturn(bookingDTOResponse);
        try {
            bookingService.addBooking(bookingDTO, user.getId());
        } catch (NotFoundException e) {
            assertThat(e).isInstanceOf(NotFoundException.class);
        }
    }

    @Test
    void addBookingTestWithWrongUser() {
        assertThrows(NotFoundException.class, () -> {
            bookingService.addBooking(bookingDTO, 100L);
        }, "Expected NotFoundException when using wrong user ID");

        bookingDTO.setItemId(100L);

        assertThrows(NotFoundException.class, () -> {
            bookingService.addBooking(bookingDTO, user.getId());
        }, "Expected NotFoundException when using wrong item ID");
    }

    @Test
    void addBookingTestWithWrongItem() {
        item = new Item();
        item.setId(1L);
        item.setName("BookingItem");
        item.setDescription("BookingItemDescription");
        item.setAvailable(false);
        item.setOwner(anotherUser);
        itemRepository.save(item);
        when(bookingMapperMock.toModel(bookingDTO)).thenReturn(booking);
        when(bookingRepositoryMock.save(booking)).thenReturn(booking);
        when(bookingMapperMock.toDTO(booking)).thenReturn(bookingDTOResponse);
        assertThrows(BadRequestException.class, () -> bookingService.addBooking(bookingDTO, user.getId()));
    }

    @Test
    void addBookingWithEndDate_Test() {
        LocalDateTime plussedDays = LocalDateTime.now().plusDays(10);
        when(bookingMapperMock.toModel(bookingDTO)).thenReturn(booking);
        when(bookingRepositoryMock.save(booking)).thenReturn(booking);
        when(bookingMapperMock.toDTO(booking)).thenReturn(bookingDTOResponse);
        bookingDTO.setStart(plussedDays);
        bookingDTO.setEnd(plussedDays);
        assertThrows(BadRequestException.class, () -> bookingService.addBooking(bookingDTO, user.getId()));
    }

    @Test
    void addBookingWithWrongBookingId_Test() {
        when(bookingMapperMock.toModel(bookingDTO)).thenReturn(booking);
        when(bookingRepositoryMock.save(booking)).thenReturn(booking);
        when(bookingMapperMock.toDTO(booking)).thenReturn(bookingDTOResponse);
        bookingDTO.setStart(LocalDateTime.now().plusDays(10));
        bookingDTO.setEnd(LocalDateTime.now().plusDays(8));
        assertThrows(BadRequestException.class, () -> bookingService.addBooking(bookingDTO, user.getId()));
    }

    @Test
    void getBookingValidId_Test() {
        when(bookingMapperMock.toDTO(booking)).thenReturn(bookingDTOResponse);
        when(bookingRepository.getBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(any(), any(), any(), any())).thenReturn(Optional.of(booking));

        BookingDTOResponse result = bookingService.getBooking(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(bookingDTOResponse);
        verify(bookingRepository, times(1)).getBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(any(), any(), any(), any());
    }

    @Test
    void getBookingInvalidId_Test() {
        Long invalidId = 10L;

        when(bookingRepository.getBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(invalidId, invalidId, invalidId, invalidId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBooking(invalidId, invalidId));

        verify(bookingRepository, times(1)).getBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(invalidId, invalidId, invalidId, invalidId);
    }



    @Test
    void updateBookingWhenRejected() {
        Booking rejectedBooking = booking.toBuilder().status(REJECTED).build();
        when(bookingRepositoryMock.findById(rejectedBooking.getId())).thenReturn(Optional.of(rejectedBooking));
        when(bookingRepositoryMock.save(rejectedBooking)).thenReturn(rejectedBooking);
        when(bookingMapperMock.toDTO(booking)).thenReturn(bookingDTOResponse);

        assertDoesNotThrow(() -> bookingService.updateBooking(rejectedBooking.getId(), false, rejectedBooking.getItem().getOwner().getId()));
    }



    @Test
    void updateBookingWithNotId() {
        Booking approvedBooking = booking.toBuilder().status(WAITING).build();
        when(bookingRepositoryMock.findById(approvedBooking.getId())).thenReturn(Optional.of(approvedBooking));
        when(bookingRepositoryMock.save(approvedBooking)).thenReturn(approvedBooking);
        when(bookingMapperMock.toDTO(booking)).thenReturn(bookingDTOResponse);
        assertThrows(NotFoundException.class, () -> bookingService.updateBooking(100L, true, booking.getItem().getOwner().getId()));
    }

    @Test
    void getBookingsTestAll() {
        when(bookingMapperMock.toDTOList(bookingRepository.findAllByBooker_Id(user.getId(), from, size))).thenReturn(List.of(bookingDTOResponse));

        List<BookingDTOResponse> result = bookingService.getBookings(user.getId(), ALL, from, size);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookingDTOResponse);
    }

    @Test
    void getBookingsTestPast() {
        LocalDateTime now = LocalDateTime.now();
        when(bookingMapperMock.toDTOList(bookingRepository.findByBooker_IdAndEndIsBefore(user.getId(), now, from, size))).thenReturn(List.of(bookingDTOResponse));

        List<BookingDTOResponse> result = bookingService.getBookings(user.getId(), PAST, from, size);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookingDTOResponse);
    }

    @Test
    void getBookingsTestFuture() {
        LocalDateTime now = LocalDateTime.now();
        when(bookingMapperMock.toDTOList(bookingRepository.findByBooker_IdAndStartIsAfter(user.getId(), now, pageable))).thenReturn(List.of(bookingDTOResponse));

        List<BookingDTOResponse> result = bookingService.getBookings(user.getId(), FUTURE, from, size);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookingDTOResponse);
    }

    @Test
    void getBookingsTestCurrent() {
        LocalDateTime now = LocalDateTime.now();
        when(bookingMapperMock.toDTOList(bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(user.getId(), now, now, pageable))).thenReturn(List.of(bookingDTOResponse));

        List<BookingDTOResponse> result = bookingService.getBookings(user.getId(), CURRENT, from, size);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookingDTOResponse);
    }

    @Test
    void getBookingsTestWaiting() {
        when(bookingMapperMock.toDTOList(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(user.getId(), WAITING, pageable))).thenReturn(List.of(bookingDTOResponse));

        List<BookingDTOResponse> result = bookingService.getBookings(user.getId(), WAITING, from, size);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookingDTOResponse);
    }

    @Test
    void getBookingsTestRejected() {
        when(bookingMapperMock.toDTOList(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(user.getId(), REJECTED, pageable))).thenReturn(List.of(bookingDTOResponse));

        List<BookingDTOResponse> result = bookingService.getBookings(user.getId(), REJECTED, from, size);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookingDTOResponse);
    }

    @Test
    void getBookingsTestUnsupportedStatus() {
        assertThrows(UnSupportedStatusException.class, () -> {
            bookingService.getBookings(user.getId(), UNSUPPORTED_STATUS, from, size);
        });
    }

    @Test
    void getOwnerBookingsTestAll() {
        when(bookingMapperMock.toDTOList(bookingRepository.getBookingByItem_Owner_Id(user.getId(), pageable))).thenReturn(List.of(bookingDTOResponse));

        List<BookingDTOResponse> result = bookingService.getOwnerBookings(user.getId(), ALL, from, size);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookingDTOResponse);
    }

    @Test
    void getOwnerBookingsTestPast() {
        LocalDateTime now = LocalDateTime.now();
        when(bookingMapperMock.toDTOList(bookingRepository.findBookingByItem_Owner_IdAndEndIsBefore(user.getId(), now, pageable))).thenReturn(List.of(bookingDTOResponse));

        List<BookingDTOResponse> result = bookingService.getOwnerBookings(user.getId(), PAST, from, size);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookingDTOResponse);
    }

    @Test
    void getOwnerBookingsTestFuture() {
        LocalDateTime now = LocalDateTime.now();
        when(bookingMapperMock.toDTOList(bookingRepository.findBookingByItem_Owner_IdAndStartIsAfter(user.getId(), now, pageable))).thenReturn(List.of(bookingDTOResponse));

        List<BookingDTOResponse> result = bookingService.getOwnerBookings(user.getId(), FUTURE, from, size);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookingDTOResponse);
    }

    @Test
    void getOwnerBookingsTestCurrent() {
        LocalDateTime now = LocalDateTime.now();
        when(bookingMapperMock.toDTOList(bookingRepository.findBookingByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(user.getId(), now, now, pageable))).thenReturn(List.of(bookingDTOResponse));

        List<BookingDTOResponse> result = bookingService.getOwnerBookings(user.getId(), CURRENT, from, size);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookingDTOResponse);
    }

    @Test
    void getOwnerBookingsTestRejected() {
        when(bookingMapperMock.toDTOList(bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(user.getId(), REJECTED, pageable))).thenReturn(List.of(bookingDTOResponse));

        List<BookingDTOResponse> result = bookingService.getOwnerBookings(user.getId(), REJECTED, from, size);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookingDTOResponse);
    }

    @Test
    void getOwnerBookingsTestWaiting() {
        when(bookingMapperMock.toDTOList(bookingRepository.findBookingByStatusOrderByIdDesc(WAITING, pageable))).thenReturn(List.of(bookingDTOResponse));

        List<BookingDTOResponse> result = bookingService.getOwnerBookings(user.getId(), WAITING, from, size);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookingDTOResponse);
    }

    @Test
    void getOwnerBookingsTestUnsupportedStatus() {
        assertThrows(UnSupportedStatusException.class, () -> {
            bookingService.getOwnerBookings(user.getId(), UNSUPPORTED_STATUS, from, size);
        });
    }
}
