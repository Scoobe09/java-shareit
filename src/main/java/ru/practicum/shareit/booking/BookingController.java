package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static ru.practicum.shareit.item.ItemConstant.X_SHARER_USER_ID;
import static ru.practicum.shareit.utils.Constant.LIMIT;
import static ru.practicum.shareit.utils.Constant.INITIAL_X;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    /**
     * Creates a new booking based on the provided booking data transfer object (DTO) and associates it with a user.
     *
     * @param userid     the ID of the user creating the booking
     * @param bookingDTO the DTO containing booking information such as dates, item details, etc.
     * @return the response DTO containing the result of the booking operation, including status and booking data
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDTOResponse createBooking(@RequestHeader(X_SHARER_USER_ID) Long userid,
                                            @Valid @RequestBody BookingDTO bookingDTO) {
        return bookingService.addBooking(bookingDTO, userid);
    }

    /**
     * Updates the approval status of a booking.
     *
     * @param bookingId the ID of the booking to be updated
     * @param approved  the new approval status of the booking
     * @param ownerId   the ID of the owner making the update
     * @return the updated booking as a BookingDTOResponse object
     */
    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDTOResponse updateBookingStatus(@PathVariable Long bookingId,
                                                  @RequestParam Boolean approved,
                                                  @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        return bookingService.updateBooking(bookingId, approved, ownerId);
    }


    /**
     * Retrieves a specific booking by its ID, ensuring that only the booking owner or the item owner can access the booking information.
     *
     * @param bookingId the ID of the booking to retrieve
     * @param userId    the ID of the user requesting the booking information
     * @return a response DTO containing the booking data if accessible by the user, otherwise might return null or an error
     */
    @GetMapping("/{bookingId}")
    public BookingDTOResponse getBooking(@PathVariable Long bookingId,
                                         @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    /**
     * Retrieves all bookings associated with a user, filtered by the state of the booking.
     *
     * @param userId the ID of the user whose bookings are to be retrieved
     * @param state  the state of bookings to filter by
     * @param from   the starting index of the result set
     * @param size   the maximum number of results to be returned
     * @return a list of booking response DTOs matching the specified criteria
     */
    @GetMapping
    public List<BookingDTOResponse> getBookings(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                @RequestParam(defaultValue = "ALL") BookingState state,
                                                @Positive @RequestParam(defaultValue = INITIAL_X) Integer from,
                                                @Positive @RequestParam(defaultValue = LIMIT) Integer size) {
        return bookingService.getBookings(userId, state, from, size);
    }

    /**
     * Retrieves all bookings for items owned by a user, filtered by the state of the booking.
     *
     * @param userId the ID of the item owner
     * @param state  the state of bookings to filter by (e.g., APPROVED, CANCELLED)
     * @param from   the start index of the results (inclusive)
     * @param size   the maximum number of results to return
     * @return a list of booking response DTOs for bookings on the user's owned items matching the specified criteria
     */
    @GetMapping("/owner")
    public List<BookingDTOResponse> getBookingsOwner(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                     @RequestParam(defaultValue = "ALL") BookingState state,
                                                     @Positive @RequestParam(defaultValue = INITIAL_X) Integer from,
                                                     @Positive @RequestParam(defaultValue = LIMIT) Integer size) {
        return bookingService.getOwnerBookings(userId, state, from, size);
    }
}