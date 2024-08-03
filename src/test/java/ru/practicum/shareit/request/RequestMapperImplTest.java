package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestMapperImplTest {

    @Test
    public void toDTO_NullInput_ReturnsNull() {
        RequestMapperImpl mapper = new RequestMapperImpl();

        Request request = null;
        RequestDTO actual = mapper.toDTO(request);

        assertNull(actual);
    }

    @Test
    public void toDTO_ValidInput_ReturnsRequestDTO() {
        RequestMapperImpl mapper = new RequestMapperImpl();

        Request request = new Request();
        request.setId(1L);
        request.setDescription("Test Description");
        request.setCreated(LocalDateTime.now());
        User user = new User();
        user.setId(1L);
        request.setRequestor(user);

        RequestDTO expected = new RequestDTO();
        expected.setId(1L);
        expected.setDescription("Test Description");
        expected.setCreated(request.getCreated());
        expected.setRequestor(user);

        RequestDTO actual = mapper.toDTO(request);

        assertEquals(expected, actual);
    }

    @Test
    public void toModel_NullInput_ReturnsNull() {
        RequestMapperImpl mapper = new RequestMapperImpl();

        RequestDTO requestDTO = null;
        Request actual = mapper.toModel(requestDTO);

        assertNull(actual);
    }

    @Test
    public void toModel_ValidInput_ReturnsRequest() {
        RequestMapperImpl mapper = new RequestMapperImpl();

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setId(1L);
        requestDTO.setDescription("Test Description");
        requestDTO.setCreated(LocalDateTime.now());
        User user = new User();
        user.setId(1L);
        requestDTO.setRequestor(user);

        Request expected = new Request();
        expected.setId(1L);
        expected.setDescription("Test Description");
        expected.setCreated(requestDTO.getCreated());
        expected.setRequestor(user);

        Request actual = mapper.toModel(requestDTO);

        assertEquals(expected, actual);
    }

    @Test
    public void toDTOList_NullInput_ReturnsNull() {
        RequestMapperImpl mapper = new RequestMapperImpl();

        List<Request> requests = null;
        List<RequestDTO> actual = mapper.toDTOList(requests);

        assertNull(actual);
    }

    @Test
    public void toDTOList_ValidInput_ReturnsListRequestDTO() {
        RequestMapperImpl mapper = new RequestMapperImpl();

        User user = new User();
        user.setId(1L);

        Request request1 = new Request();
        request1.setId(1L);
        request1.setDescription("Test Description 1");
        request1.setCreated(LocalDateTime.now());
        request1.setRequestor(user);

        Request request2 = new Request();
        request2.setId(2L);
        request2.setDescription("Test Description 2");
        request2.setCreated(LocalDateTime.now());
        request2.setRequestor(user);

        List<Request> requests = Arrays.asList(request1, request2);

        List<RequestDTO> expected = new ArrayList<>();

        RequestDTO requestDTO1 = new RequestDTO();
        requestDTO1.setId(1L);
        requestDTO1.setDescription("Test Description 1");
        requestDTO1.setCreated(request1.getCreated());
        requestDTO1.setRequestor(user);
        expected.add(requestDTO1);

        RequestDTO requestDTO2 = new RequestDTO();
        requestDTO2.setId(2L);
        requestDTO2.setDescription("Test Description 2");
        requestDTO2.setCreated(request2.getCreated());
        requestDTO2.setRequestor(user);
        expected.add(requestDTO2);

        List<RequestDTO> actual = mapper.toDTOList(requests);

        assertEquals(expected, actual);
    }

    @Test
    public void toRequestList_NullInput_ReturnsNull() {
        RequestMapperImpl mapper = new RequestMapperImpl();

        List<RequestDTO> requestDTOs = null;
        List<Request> actual = mapper.toRequestList(requestDTOs);

        assertNull(actual);
    }

    @Test
    public void toRequestList_ValidInput_ReturnsListRequest() {
        RequestMapperImpl mapper = new RequestMapperImpl();

        User user = new User();
        user.setId(1L);

        RequestDTO requestDTO1 = new RequestDTO();
        requestDTO1.setId(1L);
        requestDTO1.setDescription("Test Description 1");
        requestDTO1.setRequestor(user);

        RequestDTO requestDTO2 = new RequestDTO();
        requestDTO2.setId(2L);
        requestDTO2.setDescription("Test Description 2");
        requestDTO2.setRequestor(user);

        List<RequestDTO> requestDTOs = Arrays.asList(requestDTO1, requestDTO2);

        List<Request> expected = new ArrayList<>();

        Request request1 = new Request();
        request1.setId(1L);
        request1.setDescription("Test Description 1");
        request1.setRequestor(user);
        expected.add(request1);

        Request request2 = new Request();
        request2.setId(2L);
        request2.setDescription("Test Description 2");
        request2.setRequestor(user);
        expected.add(request2);

        List<Request> actual = mapper.toRequestList(requestDTOs);

        assertEquals(expected, actual);
    }
}
