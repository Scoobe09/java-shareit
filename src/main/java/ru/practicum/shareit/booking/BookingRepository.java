package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Updates the status of bookings with the specified ID to the given status.
     *
     * @param status The new status of the bookings.
     * @param id     The ID of the bookings to be updated.
     */
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE bookings SET status = :status WHERE id = :id")
    int updateBookingsByStatus(String status, Long id);

    /**
     * Checks if there exists a booking with the given booker ID, item ID, status, and end date before the specified date/time.
     *
     * @param bookerId  the ID of the booker
     * @param itemId    the ID of the item
     * @param status    the list of booking states to match against
     * @param endBefore the end date/time before which the booking should end
     * @return true if a booking exists with the given criteria, false otherwise
     */
    boolean existsByBookerIdAndItem_IdAndStatusInAndEndBefore(Long bookerId, Long itemId, List<BookingState> status, LocalDateTime endBefore);

    /**
     * Retrieves a Booking by the specified id and either the booker's id or the item's owner id.
     *
     * @param id       The id of the Booking to retrieve.
     * @param bookerId The id of the booker.
     * @param id2      An optional id parameter.
     * @param itemId   The id of the item's owner.
     * @return The Booking that matches the specified criteria, or null if no such Booking exists.
     */
    Optional<Booking> getBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(Long id, Long bookerId, Long id2, Long itemId);

    /**
     * Retrieves a list of bookings for a specific booker within a given time range.
     *
     * @param bookerId the ID of the booker
     * @param start    the start datetime of the time range
     * @param end      the end datetime of the time range
     * @param pageable the pagination information
     * @return a list of Booking objects that match the given criteria
     */
    List<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    /**
     * Finds all bookings associated with a specific booker ID.
     *
     * @param bookerId The ID of the booker.
     * @param start    The index of the first result to retrieve (0-indexed).
     * @param limit    The maximum number of results to retrieve.
     * @return A list of bookings associated with the booker ID, ordered by start date in descending order,
     * with pagination applied (starting from the given start index and limiting the number of results to the given limit).
     */

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM bookings b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "WHERE u.id = :bookerId " +
                    "ORDER BY b.start_date DESC " +
                    "LIMIT :limit " +
                    "OFFSET :start")
    List<Booking> findAllByBooker_Id(Long bookerId, Integer start, Integer limit);

    /**
     * Retrieves a list of bookings owned by a specific booker.
     *
     * @param bookerId The ID of the booker.
     * @param pageable The pagination information.
     * @return A list of bookings owned by the booker.
     */
    List<Booking> getBookingByItem_Owner_Id(Long bookerId, Pageable pageable);

    /**
     * Finds a list of bookings where the owner of the item is the given bookerId and the end date is before the specified end date.
     *
     * @param bookerId the ID of the booker (item owner)
     * @param end      the end date to compare with
     * @param pageable the pageable information
     * @return a list of bookings matching the criteria
     */
    List<Booking> findBookingByItem_Owner_IdAndEndIsBefore(Long bookerId, LocalDateTime end, Pageable pageable);

    /**
     * Find bookings by item owner ID and start date is after the given date.
     *
     * @param bookerId the ID of the booking owner
     * @param start    the start date to filter the bookings
     * @param pageable the pagination information
     * @return a list of bookings that match the given criteria
     */
    List<Booking> findBookingByItem_Owner_IdAndStartIsAfter(Long bookerId, LocalDateTime start, Pageable pageable);

    /**
     * Finds bookings based on the owner ID of the item, start and end date-time of the booking.
     *
     * @param bookerId The ID of the owner of the item.
     * @param start    The start date-time of the booking.
     * @param end      The end date-time of the booking.
     * @param pageable The page information for pagination and sorting.
     * @return A list of bookings that match the given criteria.
     */
    List<Booking> findBookingByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);


    /**
     * Finds a list of bookings with a specific booker ID and end date before a given date.
     *
     * @param userId The ID of the booker.
     * @param end    The end date to compare against.
     * @param start  The starting index for pagination.
     * @param limit  The maximum number of results to return.
     * @return A list of Booking objects that match the specified criteria.
     */
    @Query(nativeQuery = true,
            value = "SELECT b.* " +
                    "FROM bookings b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "WHERE u.id = :userId " +
                    "AND b.end_date < :end " +
                    "ORDER BY b.id DESC " +
                    "LIMIT :limit " +
                    "OFFSET :start")
    List<Booking> findByBooker_IdAndEndIsBefore(Long userId, LocalDateTime end, Integer start, Integer limit);

    /**
     * Finds bookings by booker ID and start date after a specified date.
     *
     * @param userId   the ID of the booker
     * @param start    the start date to filter the bookings by
     * @param pageable the pagination information for the query
     * @return a list of bookings that match the specified booker ID and start date
     */

    List<Booking> findByBooker_IdAndStartIsAfter(Long userId, LocalDateTime start, Pageable pageable);

    /**
     * Retrieves a list of bookings for a specific booker and status, ordered by start date in descending order.
     *
     * @param userId   the ID of the booker
     * @param status   the booking state
     * @param pageable the pagination information
     * @return a list of bookings
     */
    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, BookingState status, Pageable pageable);

    /**
     * Find bookings by item owner's ID and status, ordered by start date in descending order.
     *
     * @param ownerId  The ID of the item owner.
     * @param status   The booking status.
     * @param pageable The pagination information.
     * @return The list of bookings matching the criteria.
     */
    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(Long ownerId, BookingState status, Pageable pageable);

    /**
     * Finds the first booking with the given item ID where the start date is before the specified date and the status is not the given status, ordered by start date in descending
     * order.
     *
     * @param itemId The ID of the item.
     * @param start  The date to compare the start date with.
     * @param status The status to exclude.
     * @return An Optional containing the first booking that satisfies the conditions, or an empty Optional if no such booking is found.
     */
    Optional<Booking> findFirstByItem_IdAndStartIsBeforeAndStatusIsNotOrderByStartDesc(Long itemId, LocalDateTime start, BookingState status);

    /**
     * Returns the first Booking object with the given item id that has a start date after the specified start date
     * and status is not equal to the specified status. The results are ordered by start date in ascending order.
     *
     * @param itemId the item id to search for
     * @param start  the start date to compare with
     * @param status the status to exclude
     * @return the first Booking object matching the criteria, or Optional.empty() if no match is found
     */
    Optional<Booking> findFirstByItem_IdAndStartIsAfterAndStatusIsNotOrderByStartAsc(Long itemId, LocalDateTime start, BookingState status);

    /**
     * Retrieves a list of bookings based on the given list of items, booking state, and sort order of start date in ascending order.
     *
     * @param items    The list of items.
     * @param status   The booking state.
     * @param pageable The pageable object for sorting and pagination.
     * @return A list of Booking objects that match the given criteria.
     */
    List<Booking> findAllByItemInAndStatusOrderByStartAsc(List<Item> items, BookingState status, Pageable pageable);

    /**
     * Finds a list of bookings by status, ordered by id in descending order.
     *
     * @param state    The status of the bookings to find
     * @param pageable The pageable object defining pagination and sorting
     * @return A list of bookings that match the given status, ordered by id in descending order
     */
    List<Booking> findBookingByStatusOrderByIdDesc(BookingState state, Pageable pageable);
}