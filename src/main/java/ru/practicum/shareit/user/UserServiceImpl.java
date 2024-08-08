package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * The UserServiceImpl class is responsible for managing users in the application.
 * It implements the UserService interface and provides methods for adding, updating,
 * retrieving, and deleting users, as well as listing all users and checking if a user
 * exists by their ID.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = REPEATABLE_READ, propagation = REQUIRED)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;


    @Transactional
    @Override
    public UserDTO addUser(UserDTO userDTO) {
        log.debug("Adding a user with email: {}", userDTO.getEmail());

        UserDTO savedUserDTO = mapper.toDTO(repository.saveAndFlush(mapper.toModel(userDTO)));

        log.info("User added with ID: {}", savedUserDTO.getId());
        return savedUserDTO;
    }

    @Transactional
    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.debug("Updating user with ID: {}", id);

        User existingUser = (findUserById(id));
        updateUserInfoFromDTO(existingUser, userDTO);
        repository.save(existingUser);

        log.info("User updated with ID: {}", id);
        return mapper.toDTO(existingUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        log.debug("Deleting user with ID: {}", id);

        repository.deleteById(id);

        log.info("User deleted with ID: {}", id);
    }

    @Override
    public UserDTO getById(Long id) {
        log.debug("Retrieving user with ID: {}", id);

        UserDTO userDTO = mapper.toDTO(findUserById(id));

        log.info("User retrieved with ID: {}", id);
        return userDTO;
    }

    @Override
    public List<UserDTO> getAll(Integer from, Integer size) {
        log.debug("Starting method getAll with parameters from={} and size={}", from, size);

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(from, size, sort);

        Page<User> pagedResult = repository.findAll(pageable);
        List<UserDTO> userDTOList = Collections.emptyList();

        if (pagedResult != null && !pagedResult.isEmpty() && !pagedResult.getContent().isEmpty()) {
            userDTOList = mapper.toDTOList(pagedResult.getContent());
        }

        log.info("Finished method getAll, retrieved {} users", userDTOList.size());
        return userDTOList;
    }

    @Override
    public boolean isExistUser(Long id) {
        boolean exists = repository.existsById(id);

        log.debug("User existence check for ID: {} - Exists: {}", id, exists);
        return exists;
    }

    private User findUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("User not found with ID: {}", id);
            return new NotFoundException("The user was not found");
        });
    }

    private void updateUserInfoFromDTO(User user, UserDTO userDTO) {
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
    }
}