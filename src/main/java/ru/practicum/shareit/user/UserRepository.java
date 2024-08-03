package ru.practicum.shareit.user;


import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for basic user repository operations.
 * It allows for retrieving User entities by ID, fetching all Users,
 * and checking for existence by email and ID.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}