package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapperResponseImplTest {

    @Test
    void toDTO() {
        Request request = new Request();
        request.setId(1L);
        request.setDescription("Test Request Description");
        request.setCreated(LocalDateTime.now());

        RequestMapperResponseImpl mapper = new RequestMapperResponseImpl();

        RequestDTOResponse result = mapper.toDTO(request);

        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getCreated(), result.getCreated());
    }

    @Test
    void toDTO_null() {
        RequestMapperResponseImpl mapper = new RequestMapperResponseImpl();
        assertNull(mapper.toDTO(null));
    }


    @Test
    void toModel() {
        RequestDTOResponse dtoResponse = new RequestDTOResponse();
        dtoResponse.setId(1L);
        dtoResponse.setDescription("Test Request Description");
        dtoResponse.setCreated(LocalDateTime.now());

        RequestMapperResponseImpl mapper = new RequestMapperResponseImpl();

        Request result = mapper.toModel(dtoResponse);

        assertEquals(dtoResponse.getId(), result.getId());
        assertEquals(dtoResponse.getDescription(), result.getDescription());
        assertEquals(dtoResponse.getCreated(), result.getCreated());
    }

    @Test
    void toModel_null() {
        RequestMapperResponseImpl mapper = new RequestMapperResponseImpl();
        assertNull(mapper.toModel(null));
    }

    @Test
    void toDTOList() {
        Request request1 = new Request();
        request1.setId(1L);
        request1.setDescription("Test Request Description 1");
        request1.setCreated(LocalDateTime.now());

        Request request2 = new Request();
        request2.setId(2L);
        request2.setDescription("Test Request Description 2");
        request2.setCreated(LocalDateTime.now());

        List<Request> requestList = new ArrayList<>();
        requestList.add(request1);
        requestList.add(request2);

        RequestMapperResponseImpl mapper = new RequestMapperResponseImpl();

        List<RequestDTOResponse> result = mapper.toDTOList(requestList);

        assertNotNull(result);
        assertEquals(2, result.size());

        for (int i = 0; i < result.size(); i++) {
            assertEquals(requestList.get(i).getId(), result.get(i).getId());
            assertEquals(requestList.get(i).getDescription(), result.get(i).getDescription());
            assertEquals(requestList.get(i).getCreated(), result.get(i).getCreated());
        }
    }

    @Test
    void toDTOList_null() {
        RequestMapperResponseImpl mapper = new RequestMapperResponseImpl();
        assertNull(mapper.toDTOList(null));
    }
}
