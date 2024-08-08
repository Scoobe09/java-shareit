package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDTOResponse toDTO(Booking booking);

    @Mappings({@Mapping(source = "itemId", target = "item.id"),
            @Mapping(source = "bookerId", target = "booker.id")})
    Booking toModel(BookingDTO bookingDTO);

    List<BookingDTOResponse> toDTOList(List<Booking> bookingList);
}