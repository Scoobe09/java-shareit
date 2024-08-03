package ru.practicum.shareit.booking;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class BookingMapperImplTest {

    private final BookingMapperImpl bookingMapper = new BookingMapperImpl();

    @Test
    void shouldReturnBookingDTOResponse_WhenToDTOReceivesValidBooking() {
        Long expectedId = 1L;
        LocalDateTime expectedStart = LocalDateTime.now();
        LocalDateTime expectedEnd = LocalDateTime.now().plusDays(2);
        Item expectedItem = Item.builder().id(1L).build();
        User expectedUser = User.builder().id(1L).build();
        BookingState expectedStatus = BookingState.REJECTED;
        Booking booking = Booking.builder()
                .id(expectedId)
                .start(expectedStart)
                .end(expectedEnd)
                .item(expectedItem)
                .booker(expectedUser)
                .status(expectedStatus)
                .build();
        BookingDTOResponse result = bookingMapper.toDTO(booking);
        assertThat(result.getId()).isEqualTo(expectedId);
        assertThat(result.getStart()).isEqualTo(expectedStart);
        assertThat(result.getEnd()).isEqualTo(expectedEnd);
        assertThat(result.getItem().getId()).isEqualTo(expectedItem.getId());
        assertThat(result.getBooker().getId()).isEqualTo(expectedUser.getId());
        assertThat(result.getStatus()).isEqualTo(expectedStatus);
    }

    @Test
    void shouldReturnNull_WhenToDTOReceivesNull() {
        Booking booking = null;

        BookingDTOResponse result = bookingMapper.toDTO(booking);

        assertThat(result).isNull();
    }

    @Test
    void shouldReturnBooking_WhenToModelReceivesValidBookingDTO() {
        Long expectedId = 1L;
        LocalDateTime expectedStart = LocalDateTime.now();
        LocalDateTime expectedEnd = LocalDateTime.now().plusDays(2);
        Long expectedItemId = 1L;
        Long expectedUserId = 1L;
        BookingState expectedStatus = BookingState.REJECTED;

        BookingDTO bookingDTO = BookingDTO.builder()
                .id(expectedId)
                .start(expectedStart)
                .end(expectedEnd)
                .itemId(expectedItemId)
                .bookerId(expectedUserId)
                .status(expectedStatus)
                .build();

        Booking result = bookingMapper.toModel(bookingDTO);

        assertThat(result.getId()).isEqualTo(expectedId);
        assertThat(result.getStart()).isEqualTo(expectedStart);
        assertThat(result.getEnd()).isEqualTo(expectedEnd);
        assertThat(result.getItem().getId()).isEqualTo(expectedItemId);
        assertThat(result.getBooker().getId()).isEqualTo(expectedUserId);
        assertThat(result.getStatus()).isEqualTo(expectedStatus);
    }

    @Test
    void shouldReturnNull_WhenToModelReceivesNull() {
        BookingDTO bookingDTO = null;

        Booking result = bookingMapper.toModel(bookingDTO);

        assertThat(result).isNull();
    }

    @Test
    void shouldReturnDTOList_WhenToDTOListReceivesValidBookingList() {
        Long expectedIdD1 = 1L;
        LocalDateTime expectedStartD1 = LocalDateTime.now();
        LocalDateTime expectedEndD1 = LocalDateTime.now().plusDays(2);
        Item expectedItemD1 = Item.builder().id(1L).build();
        User expectedUserD1 = User.builder().id(1L).build();
        BookingState expectedStatusD1 = BookingState.REJECTED;

        Booking bookingD1 = Booking.builder()
                .id(expectedIdD1)
                .start(expectedStartD1)
                .end(expectedEndD1)
                .item(expectedItemD1)
                .booker(expectedUserD1)
                .status(expectedStatusD1)
                .build();

        Long expectedIdD2 = 2L;
        LocalDateTime expectedStartD2 = LocalDateTime.now();
        LocalDateTime expectedEndD2 = LocalDateTime.now().plusDays(2);
        Item expectedItemD2 = Item.builder().id(2L).build();
        User expectedUserD2 = User.builder().id(2L).build();
        BookingState expectedStatusD2 = BookingState.REJECTED;

        Booking bookingD2 = Booking.builder()
                .id(expectedIdD2)
                .start(expectedStartD2)
                .end(expectedEndD2)
                .item(expectedItemD2)
                .booker(expectedUserD2)
                .status(expectedStatusD2)
                .build();

        List<Booking> bookingList = Arrays.asList(bookingD1, bookingD2);

        List<BookingDTOResponse> result = bookingMapper.toDTOList(bookingList);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(expectedIdD1);
        assertThat(result.get(0).getStart()).isEqualTo(expectedStartD1);
        assertThat(result.get(0).getEnd()).isEqualTo(expectedEndD1);
        assertThat(result.get(0).getItem().getId()).isEqualTo(expectedItemD1.getId());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expectedUserD1.getId());
        assertThat(result.get(0).getStatus()).isEqualTo(expectedStatusD1);

        assertThat(result.get(1).getId()).isEqualTo(expectedIdD2);
        assertThat(result.get(1).getStart()).isEqualTo(expectedStartD2);
        assertThat(result.get(1).getEnd()).isEqualTo(expectedEndD2);
        assertThat(result.get(1).getItem().getId()).isEqualTo(expectedItemD2.getId());
        assertThat(result.get(1).getBooker().getId()).isEqualTo(expectedUserD2.getId());
        assertThat(result.get(1).getStatus()).isEqualTo(expectedStatusD2);
    }

    @Test
    void shouldReturnNull_WhenToDTOListReceivesNull() {
        List<Booking> bookingList = null;

        List<BookingDTOResponse> result = bookingMapper.toDTOList(bookingList);

        assertThat(result).isNull();
    }

}
