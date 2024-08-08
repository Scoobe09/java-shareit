package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserConstant {
    public static final String REGEX_LOGIN = "^\\S*$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
}