package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    private Integer id;
    @Pattern(regexp = "^\\S*$")
    private String name;
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат данных email")
    private String email;

}
