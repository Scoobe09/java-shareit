package ru.practicum.shareit.request;

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
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService requestService;


    /**
     * Creates a new request using the provided request data.
     *
     * @param requestDTO The data of the request to be created.
     * @param userId     The ID of the user making the request.
     * @return The response containing the newly created request.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDTOResponse createRequest(@Valid @RequestBody  RequestDTO requestDTO,
                                            @RequestHeader(X_SHARER_USER_ID)  Long userId) {
        return requestService.addRequestDTO(requestDTO, userId);
    }

    /**
     * Retrieves a list of RequestDTOResponse objects for the given user ID.
     *
     * @param userId the ID of the user making the request
     * @return a list of RequestDTOResponse objects
     */
    @GetMapping
    public List<RequestDTOResponse> getRequests(@RequestHeader(X_SHARER_USER_ID)  Long userId,
                                                @Positive @RequestParam(defaultValue = INITIAL_X)  Integer from,
                                                @Positive @RequestParam(defaultValue = LIMIT)  Integer size) {
        return requestService.getRequestsDTO(userId, from, size);
    }

    /**
     * Retrieves all requests based on the given parameters.
     *
     * @param userId The user ID.
     * @param from   The starting index of the result set. (optional)
     * @param size   The maximum number of requests to retrieve. (optional)
     * @return A list of RequestDTOResponse objects.
     */
    @GetMapping("/all")
    public List<RequestDTOResponse> getAllRequests(@RequestHeader(X_SHARER_USER_ID)  Long userId,
                                                   @Positive @RequestParam(defaultValue = INITIAL_X)  Integer from,
                                                   @Positive @RequestParam(defaultValue = LIMIT)  Integer size) {
        return requestService.getAllRequestsPagableDTO(userId, from, size);
    }

    /**
     * Retrieves a specific request by its ID.
     *
     * @param requestId the ID of the request
     * @param userId    the ID of the user making the request
     * @return the RequestDTOResponse object representing the request
     */
    @GetMapping("/{requestId}")
    public RequestDTOResponse getRequest(@PathVariable  Long requestId,
                                         @RequestHeader(X_SHARER_USER_ID)  Long userId,
                                         @Positive @RequestParam(defaultValue = INITIAL_X) Integer from,
                                         @Positive @RequestParam(defaultValue = LIMIT) Integer size) {
        return requestService.getRequestById(requestId, userId, from, size);
    }
}
