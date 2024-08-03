
package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingMapperShortDTOImplTest {
    private final BookingMapperShortDTOImpl bookingMapper = new BookingMapperShortDTOImpl();
    private Booking booking;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        user = new User();
        user.setId(1L);
        booking.setBooker(user);
    }

    @Test
    public void whenToDTONotNull_thenPropertiesMatches() {
        BookingShortDTO result = bookingMapper.toDTO(booking);
        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    public void whenToDTONull_thenReturnsNull() {
        BookingShortDTO result = bookingMapper.toDTO(null);
        assertNull(result);
    }

}
