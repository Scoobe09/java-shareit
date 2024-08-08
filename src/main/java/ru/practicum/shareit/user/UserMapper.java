package ru.practicum.shareit.user;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Converts a User object to a UserDTO object.
     *
     * @param user the User object to be converted
     * @return the converted UserDTO object
     */
    @InheritInverseConfiguration
    UserDTO toDTO(User user);

    /**
     * Converts a UserDTO object to a User object.
     *
     * @param userDTO the UserDTO object to be converted
     * @return the User object converted from the UserDTO object
     */
    User toModel(UserDTO userDTO);

    /**
     * Converts a list of User entities to a list of UserDTO objects.
     *
     * @param modelList the list of User entities to be converted
     * @return a list of UserDTO objects
     */
    @IterableMapping(elementTargetType = UserDTO.class)
    List<UserDTO> toDTOList(List<User> modelList);

    /**
     * Converts a list of UserDTO objects to a list of User objects.
     *
     * @param userDTOList the list of UserDTO objects to convert
     * @return the converted list of User objects
     */
    List<User> toUserList(List<UserDTO> userDTOList);
}