package ru.practicum.shareit.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static ru.practicum.shareit.user.UserConstant.REGEX_EMAIL;
import static ru.practicum.shareit.user.UserConstant.REGEX_LOGIN;
import static ru.practicum.shareit.utils.Marker.OnCreate;
import static ru.practicum.shareit.utils.Marker.OnUpdate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "id")
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id", "email"})
public class UserDTO {

    private Long id;

    @NotBlank(message = "Name cannot be blank", groups = OnCreate.class)
    @Pattern(regexp = REGEX_LOGIN, message = "This name is incorrect", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "Email cannot be null", groups = OnCreate.class)
    @Email(regexp = REGEX_EMAIL, message = "Non standard writing of mail", groups = {OnCreate.class, OnUpdate.class})
    private String email;
}