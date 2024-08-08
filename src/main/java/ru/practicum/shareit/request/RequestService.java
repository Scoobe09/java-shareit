package ru.practicum.shareit.request;

import java.util.List;

/**
 * The RequestService interface defines the methods for managing requests.
 */
public interface RequestService {

    /**
     * Creates a new request using the provided request data.
     *
     * @param request The data of the request to be created.
     * @param userId  The ID of the user making the request.
     * @return The response containing the newly created request.
     */
    RequestDTOResponse addRequestDTO(RequestDTO request, Long userId);

    /**
     * Retrieves a list of RequestDTOResponse objects for the given user ID.
     *
     * @param userId the ID of the user making the request
     * @return a list of RequestDTOResponse objects
     */
    List<RequestDTOResponse> getRequestsDTO(Long userId, Integer from, Integer size);

    /**
     * Retrieves all requests based on the given parameters.
     *
     * @param userId The user ID.
     * @param from   The starting index of the result set. (optional)
     * @param size   The maximum number of requests to retrieve. (optional)
     * @return A list of RequestDTOResponse objects.
     */
    List<RequestDTOResponse> getAllRequestsPagableDTO(Long userId, Integer from, Integer size);

    /**
     * Retrieves a specific request by its ID.
     *
     * @param requestId the ID of the request
     * @param userId    the ID of the user making the request
     * @return the RequestDTOResponse object representing the request
     */
    RequestDTOResponse getRequestById(Long requestId, Long userId, Integer from, Integer size);
}
