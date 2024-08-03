package ru.practicum.shareit.booking;

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

import java.time.LocalDateTime;
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
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void createBooking() {
        Long userId = 1L;
        BookingDTO newBooking = BookingDTO.builder()
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        BookingDTOResponse bookingDTOResponse = BookingDTOResponse.builder()
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        when(bookingService.addBooking(any(BookingDTO.class), eq(userId))).thenReturn(bookingDTOResponse);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTOResponse))
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    void updateBookingStatus() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean newStatus = true;
        BookingDTO newBooking = BookingDTO.builder()
                .id(bookingId)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        BookingDTOResponse bookingDTOResponse = BookingDTOResponse.builder()
                .id(bookingId)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        when(bookingService.updateBooking(eq(newBooking.getId()), eq(newStatus), eq(userId))).thenReturn(bookingDTOResponse);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", newStatus.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDTOResponse)));
    }

    @Test
    @SneakyThrows
    void getBooking() {
        Long userId = 1L;
        Long bookingId = 1L;

        BookingDTOResponse bookingDTOResponse = BookingDTOResponse.builder()
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        when(bookingService.getBooking(eq(bookingId), eq(userId))).thenReturn(bookingDTOResponse);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDTOResponse)));
    }

    @Test
    @SneakyThrows
    void getBookings() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        BookingDTOResponse bookingDTOResponse = BookingDTOResponse.builder()
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .build();
        List<BookingDTOResponse> bookingDTOResponseList = List.of(bookingDTOResponse);


        when(bookingService.getBookings(eq(userId), eq(BookingState.ALL), eq(from), eq(size))).thenReturn(bookingDTOResponseList);


        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDTOResponseList)));
    }

    @Test
    @SneakyThrows
    void getBookingsOwnerTest() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 20;
        BookingState state = BookingState.ALL;

        BookingDTOResponse bookingDTOResponse = BookingDTOResponse.builder()
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .build();
        List<BookingDTOResponse> bookingDTOResponseList = List.of(bookingDTOResponse);

        when(bookingService.getOwnerBookings(eq(userId), eq(state), eq(from), eq(size))).thenReturn(bookingDTOResponseList);

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, userId)
                        .param("state", state.name())
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDTOResponseList)));
    }
}