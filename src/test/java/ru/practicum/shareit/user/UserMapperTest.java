package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {
    private static final UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    @Test
    void toDTO() {
        User user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("johndoe@example.com")
                .build();

        UserDTO userDTO = USER_MAPPER.toDTO(user);

        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    void toDTO_nullObject() {
        User user = null;

        UserDTO userDTO = USER_MAPPER.toDTO(user);

        assertNull(userDTO);
    }

    @Test
    void toModel() {
        UserDTO userDTO = UserDTO.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .build();

        User user = USER_MAPPER.toModel(userDTO);

        assertNotNull(user);
        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getEmail(), user.getEmail());
    }

    @Test
    void toModel_nullObject() {
        UserDTO userDTO = null;

        User user = USER_MAPPER.toModel(userDTO);

        assertNull(user);
    }

    @Test
    void toDTOList() {
        User user1 = User.builder()
                .id(1L)
                .name("John Doe")
                .email("johndoe@example.com")
                .build();
        User user2 = User.builder()
                .id(2L)
                .name("Jane Doe")
                .email("janedoe@example.com")
                .build();
        List<User> userList = Arrays.asList(user1, user2);

        List<UserDTO> userDTOList = USER_MAPPER.toDTOList(userList);

        assertNotNull(userDTOList);
        assertEquals(2, userDTOList.size());
        assertEquals(user1.getName(), userDTOList.get(0).getName());
        assertEquals(user2.getName(), userDTOList.get(1).getName());
    }

    @Test
    void toDTOList_emptyList() {
        List<User> userList = new ArrayList<>();

        List<UserDTO> userDTOList = USER_MAPPER.toDTOList(userList);

        assertNotNull(userDTOList);
        assertTrue(userDTOList.isEmpty());
    }

    @Test
    void toDTOList_nullList() {
        // When
        List<UserDTO> userDTOList = USER_MAPPER.toDTOList(null);

        // Then
        assertNull(userDTOList);
    }

    @Test
    void toUserList() {
        // Given
        UserDTO userDTO1 = UserDTO.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .build();
        UserDTO userDTO2 = UserDTO.builder()
                .name("Jane Doe")
                .email("janedoe@example.com")
                .build();
        List<UserDTO> userDTOList = Arrays.asList(userDTO1, userDTO2);

        // When
        List<User> userList = USER_MAPPER.toUserList(userDTOList);

        // Then
        assertNotNull(userList);
        assertEquals(2, userList.size());
        assertEquals(userDTO1.getName(), userList.get(0).getName());
        assertEquals(userDTO2.getName(), userList.get(1).getName());
    }

    @Test
    void toUserList_emptyList() {
        // Given
        List<UserDTO> userDTOList = new ArrayList<>();

        // When
        List<User> userList = USER_MAPPER.toUserList(userDTOList);

        // Then
        assertNotNull(userList);
        assertTrue(userList.isEmpty());
    }

    @Test
    void toUserList_nullList() {
        // When
        List<User> userList = USER_MAPPER.toUserList(null);

        // Then
        assertNull(userList);
    }
}
