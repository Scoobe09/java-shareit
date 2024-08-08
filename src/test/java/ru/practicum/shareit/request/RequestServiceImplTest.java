package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private RequestMapper requestMapper;
    @Mock
    private RequestMapperResponse requestMapperResponse;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private RequestServiceImpl requestServiceImpl;
    private Request request;
    private RequestDTO requestDTOResponse;
    private RequestDTOResponse outputResponse;
    private RequestDTO getRequestDTOResponse;
    private Request afterMapping;
    private static final LocalDateTime NOW = LocalDateTime.now();


    @BeforeEach
    void setUp() {
        request = Request.builder()
                .id(1L)
                .description("Java is the programming language")
                .created(NOW)
                .requestor(User.builder()
                        .id(1L)
                        .name("Scoobe")
                        .email("scoobe@example.com")
                        .build())
                .build();
        requestDTOResponse = RequestDTO.builder()
                .description("Java is the programming language")
                .build();
        outputResponse = RequestDTOResponse.builder()
                .description("Java is the programming language")
                .created(NOW)
                .items(new ArrayList<>())
                .build();
        afterMapping = Request.builder()
                .description("Java is the programming language")
                .build();
        getRequestDTOResponse = RequestDTO.builder()
                .description("Java is the programming language")
                .created(NOW)
                .build();

    }

    @Test
    void addRequestDTO_success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(request.getRequestor()));
        when(requestMapper.toModel(any(RequestDTO.class))).thenReturn(afterMapping);
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(requestMapperResponse.toDTO(any(Request.class))).thenReturn(outputResponse);

        assertEquals(outputResponse, requestServiceImpl.addRequestDTO(requestDTOResponse, 1L));

        verify(userRepository).findById(1L);
        verify(requestMapper).toModel(any(RequestDTO.class));
        verify(requestRepository).save(afterMapping);
        verify(requestMapperResponse).toDTO(request);
    }


    @Test
    void addRequestDTO_whenUserNotFound_thenThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestServiceImpl.addRequestDTO(requestDTOResponse, 1L));
    }


    @Test
    void getRequestsDTO_Success() {
        User user = User.builder()
                .id(1L)
                .name("Scoobe")
                .email("scoobe@example.com")
                .build();

        Request request1 = Request.builder()
                .id(1L)
                .description("Object Oriented Design")
                .created(NOW)
                .requestor(user)
                .build();

        Request request2 = Request.builder()
                .id(2L)
                .description("Data Structures")
                .created(NOW)
                .requestor(user)
                .build();

        List<Request> requestList = new ArrayList<>(Arrays.asList(request1, request2));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestMapperResponse.toDTOList(requestList)).thenReturn(Arrays.asList(outputResponse));
        when(requestRepository.findAllByRequestor_Id(anyLong(), any())).thenReturn(new PageImpl<>(requestList));
        assertEquals(outputResponse, requestServiceImpl.getRequestsDTO(1L, 0, 1).get(0));
        verify(userRepository).findById(1L);
        verify(requestRepository).findAllByRequestor_Id(1L, PageRequest.of(0, 1));
    }

    @Test
    void getRequestsDTO_whenUserNotFound_thenThrowsException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestServiceImpl.getRequestsDTO(1L, 0, 1));
    }

    @Test
    void getAllRequestsDTOWhenUserNotFoundThenThrowsException() {
        when(requestRepository.findAllByRequestor_IdNot(anyLong(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
        assertTrue(requestServiceImpl.getAllRequestsPagableDTO(1L, 0, 1).isEmpty());
        verify(requestRepository).findAllByRequestor_IdNot(1L, PageRequest.of(0, 1));
    }

    @Test
    void getRequestById_found_success() {
        User user = User.builder().id(1L).name("Scoobe").email("scoobe@example.com").build();
        Request request1 = Request.builder()
                .id(1L)
                .description("Object Oriented Design")
                .created(NOW)
                .requestor(user)
                .build();
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findAllByRequestId(anyLong(), any())).thenReturn(new ArrayList<>());
        when(requestMapperResponse.toDTO(any(Request.class))).thenReturn(outputResponse);
        assertEquals(outputResponse, requestServiceImpl.getRequestById(1L, 1L, 0, 1));
        verify(requestRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(itemRepository).findAllByRequestId(1L, PageRequest.of(0, 1));
        verify(requestMapperResponse).toDTO(request1);
    }


    @Test
    void getRequestById_whenUserNotFound_thenThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestServiceImpl.getRequestById(1L, 1L, 0, 1));
    }
}
