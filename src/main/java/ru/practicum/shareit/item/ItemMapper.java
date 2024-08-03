package ru.practicum.shareit.item;

import org.mapstruct.Mapper;

import java.util.List;

import static ru.practicum.shareit.utils.Constant.SPRING;


@Mapper(componentModel = SPRING)
public interface ItemMapper {

    Item toModel(ItemDTO itemDto);

    ItemDTO toDTO(Item item);

    List<ItemDTO> toListDTO(List<Item> modelList);

    List<Item> toItemList(List<ItemDTO> itemDTOList);
}
