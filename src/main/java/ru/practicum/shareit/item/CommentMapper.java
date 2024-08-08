package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

import static ru.practicum.shareit.utils.Constant.SPRING;

@Mapper(componentModel = SPRING)
public interface CommentMapper {
    /**
     * Converts a Comment entity to a CommentDTO object.
     *
     * @param comment The Comment entity to be converted
     * @return The CommentDTO object
     */
    @Mappings({@Mapping(source = "author.id", target = "authorId"),
            @Mapping(source = "author.name", target = "authorName"),
            @Mapping(source = "item.id", target = "itemId")})
    CommentDTO toDTO(Comment comment);

    /**
     * Converts a CommentDTO object to a Comment object.
     *
     * @param commentDTO The CommentDTO object to be converted.
     * @return The converted Comment object.
     */
    @Mappings({@Mapping(source = "authorId", target = "author.id"),
            @Mapping(source = "authorName", target = "author.name"),
            @Mapping(source = "itemId", target = "item.id")})
    Comment toModel(CommentDTO commentDTO);

    /**
     * Converts a list of Comment objects to a list of CommentDTO objects.
     *
     * @param comments the list of Comment objects to be converted
     * @return the list of CommentDTO objects
     */
    List<CommentDTO> toDTOList(List<Comment> comments);
}