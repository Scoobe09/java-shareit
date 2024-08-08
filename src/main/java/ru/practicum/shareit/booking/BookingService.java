package ru.practicum.shareit.booking;


import java.util.List;

/**
 * Defines the contract for the BookingService which handles all operations related to managing bookings.
 * This includes adding, updating, retrieving, and listing bookings based on various criteria.
 * The service is intended to encapsulate the business logic associated with booking actions and provide
 * a clear API for interaction with the booking functionalities of the application.
 * Methods within this interface ensure that only authorized users can perform operations on bookings,
 * adhering to rules such as owners managing their listings' bookings and users managing their own bookings.
 */
public interface BookingService {
    /**
     * Adds a new booking based on the provided booking data transfer object (DTO) and associates it with a user.
     *
     * @param bookingDTO the DTO containing booking information such as dates, item details, etc.
     * @param userId     the ID of the user creating the booking
     * @return a response DTO containing the result of the booking operation, including status and booking data
     */
    BookingDTOResponse addBooking(BookingDTO bookingDTO, Long userId);

    /**
     * Updates an existing booking's approval status based on the booking ID. Only accessible to the owner of the booked item or an administrator.
     *
     * @param bookingId the ID of the booking to be updated
     * @param approved  the new approval status of the booking (true or false)
     * @param userId    the ID of the user requesting the update (typically the item owner or an administrator)
     * @return a response DTO containing the updated booking data and status
     */
    BookingDTOResponse updateBooking(Long bookingId, Boolean approved, Long userId);

    /**
     * Retrieves a specific booking by its ID, ensuring that only the booking owner or the item owner can access the booking information.
     *
     * @param bookingId the ID of the booking to retrieve
     * @param userId    the ID of the user requesting the booking information
     * @return a response DTO containing the booking data if accessible by the user, otherwise might return null or an error
     */
    BookingDTOResponse getBooking(Long bookingId, Long userId);

    /**
     * Retrieves all bookings associated with a user, filtered by the state of the booking (e.g., APPROVED, REJECTED).
     *
     * @param userId the ID of the user whose bookings are to be retrieved
     * @param state  the state of bookings to filter by
     * @return a list of booking response DTOs matching the specified criteria
     */
    List<BookingDTOResponse> getBookings(Long userId, BookingState state, Integer from, Integer size);

    /**
     * Retrieves all bookings for items owned by a user, filtered by the state of the booking.
     *
     * @param userId the ID of the item owner
     * @param state  the state of bookings to filter by (e.g., PENDING, CANCELLED)
     * @return a list of booking response DTOs for bookings on the user's owned items matching the specified criteria
     */
    List<BookingDTOResponse> getOwnerBookings(Long userId, BookingState state, Integer from, Integer size);
}