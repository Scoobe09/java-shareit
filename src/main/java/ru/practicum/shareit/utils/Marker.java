package ru.practicum.shareit.utils;

/**
 * The Marker interface is a part of the ru.practicum.shareit.utils package. It is used to mark classes or
 * methods with different types of annotations.
 * <p>
 * The Marker interface contains two nested interfaces: OnCreate and OnUpdate.
 * <p>
 * The OnCreate interface is used to annotate methods that are executed during the creation process.
 * <p>
 * The OnUpdate interface is used to annotate methods that are executed during the update process.
 * <p>
 * Usage:
 * - To mark a class or method as being related to the creation process, use the OnCreate annotation.
 * - To mark a class or method as being related to the update process, use the OnUpdate annotation.
 * <p>
 * Note: This interface does not declare any methods or provide any implementation details.
 */
public interface Marker {

    interface OnCreate {
    }

    interface OnUpdate {
    }
}