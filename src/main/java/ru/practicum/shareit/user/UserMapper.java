package ru.practicum.shareit.user;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDTO(User user);

    User toModel(UserDto userDto);

    List<UserDto> toDtoList(List<User> modelList);

    List<User> toModelList(List<UserDto> dtoList);

}
