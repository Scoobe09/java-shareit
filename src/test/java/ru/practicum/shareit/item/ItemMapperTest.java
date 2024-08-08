package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {
    private static final ItemMapper ITEM_MAPPER = Mappers.getMapper(ItemMapper.class);

    private static final String SCOOBE = "SCOOBE";

    @Test
    void toDTO() {
        Item item = Item.builder()
                .id(1L)
                .name(SCOOBE)
                .available(true)
                .build();

        ItemDTO userDTO = ITEM_MAPPER.toDTO(item);

        assertNotNull(userDTO);
        assertEquals(item.getId(), userDTO.getId());
        assertEquals(item.getName(), userDTO.getName());
        assertEquals(item.getAvailable(), userDTO.getAvailable());
    }

    @Test
    void toDTO_nullObject() {
        Item user = null;

        ItemDTO userDTO = ITEM_MAPPER.toDTO(user);

        assertNull(userDTO);
    }

    @Test
    void toModel() {
        ItemDTO itemDTO = ItemDTO.builder()
                .name(SCOOBE)
                .available(true)
                .build();

        Item item = ITEM_MAPPER.toModel(itemDTO);

        assertNotNull(item);
        assertEquals(itemDTO.getName(), item.getName());
        assertEquals(itemDTO.getAvailable(), item.getAvailable());
    }

    @Test
    void toModel_nullObject() {
        ItemDTO itemDTO = null;

        Item item = ITEM_MAPPER.toModel(itemDTO);

        assertNull(item);
    }

    @Test
    void toDTOList() {
        // Given
        Item item = Item.builder()
                .id(1L)
                .name(SCOOBE)
                .available(true)
                .build();
        Item item1 = Item.builder()
                .id(2L)
                .name("Jane Doe")
                .available(true)
                .build();

        List<Item> itemList = Arrays.asList(item, item1);

        List<ItemDTO> itemDTOs = ITEM_MAPPER.toListDTO(itemList);

        assertNotNull(itemDTOs);
        assertEquals(2, itemDTOs.size());
        assertEquals(item.getName(), itemDTOs.get(0).getName());
        assertEquals(item1.getName(), itemDTOs.get(1).getName());
    }

    @Test
    void toDTOList_emptyList() {
        List<Item> items = new ArrayList<>();

        List<ItemDTO> itemMapperListDTO = ITEM_MAPPER.toListDTO(items);

        assertNotNull(itemMapperListDTO);
        assertTrue(itemMapperListDTO.isEmpty());
    }

    @Test
    void toDTOList_nullList() {
        List<ItemDTO> itemMapperListDTO = ITEM_MAPPER.toListDTO(null);

        assertNull(itemMapperListDTO);
    }

    @Test
    void toUserList() {
        ItemDTO itemDTO = ItemDTO.builder()
                .name(SCOOBE)
                .available(true)
                .build();
        ItemDTO itemDTO1 = ItemDTO.builder()
                .name(SCOOBE)
                .available(true)
                .build();
        List<ItemDTO> itemDTOS = Arrays.asList(itemDTO, itemDTO1);

        List<Item> modelList = ITEM_MAPPER.toItemList(itemDTOS);

        assertNotNull(modelList);
        assertEquals(2, modelList.size());
        assertEquals(itemDTO.getName(), modelList.get(0).getName());
        assertEquals(itemDTO1.getName(), modelList.get(1).getName());
    }

    @Test
    void toUserList_emptyList() {
        List<ItemDTO> itemDTOS = new ArrayList<>();


        List<Item> modelList = ITEM_MAPPER.toItemList(itemDTOS);

        assertNotNull(modelList);
        assertTrue(modelList.isEmpty());
    }

    @Test
    void toUserList_nullList() {
        List<Item> modelList = ITEM_MAPPER.toItemList(null);

        assertNull(modelList);
    }
}