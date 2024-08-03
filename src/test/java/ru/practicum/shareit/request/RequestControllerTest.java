package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.ItemConstant.X_SHARER_USER_ID;

@AutoConfigureMockMvc
@WebMvcTest(controllers = RequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private RequestService requestService;


    private User user;
    private RequestDTOResponse request;
    private Long requestId = 1L;
    private Long userId = 1L;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("user")
                .email("user@email.com")
                .build();
        request = RequestDTOResponse.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(LocalDateTime.now())
                .build();
        when(requestService.addRequestDTO(any(RequestDTO.class), any(Long.class))).thenReturn(request);
        when(requestService.getRequestById(any(Long.class), any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(request);
        when(requestService.addRequestDTO(any(RequestDTO.class), any(Long.class))).thenReturn(request);
    }


    @Test
    @SneakyThrows
    @DisplayName("Создание запроса: при корректном вводе должен возвращаться статус 201 Created")
    void createRequest() {
        Long userId = 1L;
        when(requestService.addRequestDTO(any(RequestDTO.class), any(Long.class))).thenReturn(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isCreated());

    }

    @Test
    @SneakyThrows
    @DisplayName("Retrieval of requests: should return the status 200 OK and expected list of requests")
    void getRequests() {
        List<RequestDTOResponse> expectedRequests = Collections.singletonList(request);
        when(requestService.getRequestsDTO(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(expectedRequests);

        mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedRequests)));
    }

    @Test
    @SneakyThrows
    @DisplayName("Retrieval of all requests: should return the status 200 OK and expected list of requests")
    void getAllRequests() {
        List<RequestDTOResponse> expectedRequests = Collections.singletonList(request);
        when(requestService.getAllRequestsPagableDTO(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(expectedRequests);

        mockMvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedRequests)));
    }

    @Test
    @SneakyThrows
    @DisplayName("Retrieval of a specific request: should return the status 200 OK and expected request")
    void getRequest() {
        when(requestService.getRequestById(any(Long.class), any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(request);

        mockMvc.perform(MockMvcRequestBuilders.get("/requests/" + request.getId())
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(request)));
    }
}