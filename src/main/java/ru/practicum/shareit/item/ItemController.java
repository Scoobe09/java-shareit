package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static ru.practicum.shareit.item.ItemConstant.X_SHARER_USER_ID;
import static ru.practicum.shareit.utils.Constant.LIMIT;
import static ru.practicum.shareit.utils.Constant.INITIAL_X;
import static ru.practicum.shareit.utils.Marker.OnCreate;
import static ru.practicum.shareit.utils.Marker.OnUpdate;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    /**
     * Creates a new item and associates it with a specific user.
     *
     * @param userId  the ID of the user who owns the item
     * @param itemDTO the item to create, encapsulated in a {@link ItemDTO}
     * @return the created item, encapsulated in a {@link ItemDTO} with its newly assigned ID
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO createItem(@RequestHeader(X_SHARER_USER_ID)  Long userId,
                              @Validated(OnCreate.class) @RequestBody  ItemDTO itemDTO) {
        return itemService.addItem(userId, itemDTO);
    }

    /**
     * Updates the details of an existing item.
     *
     * @param userId  the ID of the user who owns the item
     * @param itemId  the ID of the item to update
     * @param itemDTO the new details of the item, encapsulated in a {@link ItemDTO}
     * @return the updated item details, encapsulated in a {@link ItemDTO}
     */
    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO updateItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                              @PathVariable Long itemId,
                              @Validated(OnUpdate.class) @RequestBody ItemDTO itemDTO) {
        return itemService.updateItem(userId, itemId, itemDTO);
    }

    /**
     * Retrieves a specific item by its ID.
     *
     * @param itemId the ID of the item to retrieve
     * @param userId the ID of the user making the request
     * @return the requested item, encapsulated in a {@link ItemDTO}
     */
    @GetMapping("/{itemId}")
    public ItemDTO getById(@PathVariable  Long itemId, @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.findItemById(itemId, userId);
    }

    /**
     * Retrieves all items owned by a specific user.
     *
     * @param userId the ID of the user whose items to retrieve
     * @return a list of items owned by the user, each encapsulated in a {@link ItemDTO}
     */
    @GetMapping
    public List<ItemDTO> getAllItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                    @Positive @RequestParam(defaultValue = INITIAL_X) Integer from,
                                    @Positive @RequestParam(defaultValue = LIMIT) Integer size) {
        return itemService.getItems(userId, from, size);
    }

    /**
     * Searches for items by matching the provided text against their names and descriptions.
     *
     * @param text the text to match against item names and descriptions
     * @return a list of items that match the criteria, each encapsulated in a {@link ItemDTO}
     */
    @GetMapping("/search")
    public List<ItemDTO> searchByText(@RequestParam String text,
                                      @Positive @RequestParam(defaultValue = INITIAL_X) Integer from,
                                      @Positive @RequestParam(defaultValue = LIMIT) Integer size) {
        return itemService.getItemsByNameOrDescription(text, from, size);
    }

    /**
     * Creates a new comment for a specified item.
     *
     * @param userId     the ID of the user creating the comment
     * @param itemId     the ID of the item for which the comment is being created
     * @param commentDTO the comment details, encapsulated in a {@link CommentDTO} object
     * @return the created comment, encapsulated in a {@link CommentDTO} object
     */
    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDTO createComment(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                    @PathVariable Long itemId,
                                    @Valid @RequestBody CommentDTO commentDTO) {
        return commentService.addComment(userId, itemId, commentDTO);
    }
}