package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Finds all items by the owner's ID and returns them in ascending order by ID.
     *
     * @param id the ID of the owner
     * @return a list of items owned by the owner, ordered by ID
     */
    List<Item> findAllByOwnerIdOrderById(Long id, Pageable pageable);

    /**
     * Finds items by name containing ignore case and available is true, or description containing ignore case and available is true.
     *
     * @param name        The name to search for. Can be partial or case-insensitive.
     * @param description The description to search for. Can be partial or case-insensitive.
     * @return A list of items matching the search criteria.
     */
    List<Item> findByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description, Pageable pageable);

    /**
     * Retrieves all items by request ID.
     *
     * @param id The ID of the request.
     * @return A list of items matching the given request ID.
     */
    List<Item> findAllByRequestId(Long id, Pageable pageable);

    /**
     * Finds all items owned by a specific user and with a specific request ID.
     *
     * @param requestId The ID of the request.
     * @return A list of items matching the owner ID and request ID.
     */
    List<Item> findAllByOwner_IdAndRequestId(Long requestId, Long id);
}