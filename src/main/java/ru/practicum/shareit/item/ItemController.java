package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @Valid @RequestBody ItemDto itemDTO) {
        return itemService.createItem(userId, itemDTO);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @PathVariable Integer itemId,
                              @RequestBody ItemDto itemDTO) {
        itemDTO.setId(itemId);
        return itemService.updateItem(userId, itemId, itemDTO);
    }

    @GetMapping("/{itemId}")
    public ItemDto getByID(@PathVariable Integer itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItem(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestParam String text) {
        return itemService.getItemsByNameOrDescription(text);
    }
}
