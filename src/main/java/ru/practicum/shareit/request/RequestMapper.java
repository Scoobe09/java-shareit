package ru.practicum.shareit.request;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    RequestDTO toDTO(Request user);

    Request toModel(RequestDTO userDTO);

    List<RequestDTO> toDTOList(List<Request> modelList);

    List<Request> toRequestList(List<RequestDTO> userDTOList);
}
