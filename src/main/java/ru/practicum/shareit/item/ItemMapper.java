package ru.practicum.shareit.item;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto toDto(Item item);

    Item toModel(ItemDto itemDTO);

    List<ItemDto> toListDto(List<Item> modelList);

    List<Item> toModelList(List<ItemDto> itemDTOList);
}
