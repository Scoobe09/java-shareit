package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemDTO;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserDTO;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = REPEATABLE_READ, propagation = REQUIRED)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final RequestMapperResponse requestMapperResponse;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    @Override
    public RequestDTOResponse addRequestDTO(RequestDTO requestDTO, Long userId) {
        log.debug("Attempting to add request for user with id: {}", userId);

        UserDTO userDTO = findUserDTO(userId);

        requestDTO.setRequestor(userMapper.toModel(userDTO));
        requestDTO.setCreated(LocalDateTime.now());
        RequestDTOResponse responseDTO = requestMapperResponse.toDTO(requestRepository.save(requestMapper.toModel(requestDTO)));
        log.info("Request saved for user id: {}", userId);

        log.info("RequestDTOResponse created with created date set to now: {}", responseDTO);
        return responseDTO;
    }

    @Override
    public List<RequestDTOResponse> getRequestsDTO(Long userId, Integer from, Integer size) {
        log.debug("Fetching requests for user ID: {}", userId);
        findUserDTO(userId);

        Pageable pageable = PageRequest.of(from, size);

        List<RequestDTOResponse> dtoList = requestMapperResponse.toDTOList(requestRepository.findAllByRequestor_Id(userId, pageable).getContent());
        List<ItemDTO> listDTO = itemMapper.toListDTO(itemRepository.findAllByRequestId(userId, pageable));

        List<RequestDTOResponse> collect = getListRequestDTOWithItems(dtoList, listDTO);

        log.info("Returning {} request(s) for user ID: {}", dtoList.size(), userId);
        return collect;
    }

    @Override
    public List<RequestDTOResponse> getAllRequestsPagableDTO(Long userId, Integer from, Integer size) {
        log.debug("Fetching requests for user ID: {} from page {} with size {}", userId, from, size);

        Pageable pageable = PageRequest.of(from, size);
        List<RequestDTOResponse> list = requestMapperResponse.toDTOList(requestRepository.findAllByRequestor_IdNot(userId,
                pageable).getContent());

        Long first = list.stream()
                .map(RequestDTOResponse::getId)
                .findFirst()
                .orElse(null);

        List<ItemDTO> allByOwnerId = itemMapper.toListDTO(itemRepository.findAllByOwner_IdAndRequestId(userId, first));

        List<RequestDTOResponse> collect = getListRequestDTOWithItems(list, allByOwnerId);

        log.info("Returning {} request(s) for user id: {}", list.size(), userId);
        return collect;
    }

    @Override
    public RequestDTOResponse getRequestById(Long requestId, Long userId, Integer from, Integer size) {
        log.debug("Starting to handle getRequestById for requestId: {}, userId: {}", requestId, userId);
        findUserDTO(userId);

        Pageable pageable = PageRequest.of(from, size);

        RequestDTOResponse requestMapperResponseDTO = requestMapperResponse.toDTO(requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found")));
        log.debug("Request with id: {} converted to DTO", requestId);

        List<ItemDTO> listDTO = itemMapper.toListDTO(itemRepository.findAllByRequestId(requestId, pageable));
        requestMapperResponseDTO.setItems(listDTO);
        log.debug("Items for request id: {} retrieved and set", requestId);

        log.info("getRequestById completed successfully for requestId: {}", requestId);
        return requestMapperResponseDTO;
    }

    private UserDTO findUserDTO(Long userId) {
        return userMapper.toDTO(userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id: {} not found", userId);
            return new NotFoundException("User not found with id: " + userId);
        }));
    }

    private List<RequestDTOResponse> getListRequestDTOWithItems(List<RequestDTOResponse> dtoList, List<ItemDTO> itemDTOS) {
        return dtoList.stream()
                .peek(x -> x.setItems(itemDTOS != null ? itemDTOS : Collections.emptyList()))
                .collect(Collectors.toList());
    }
}