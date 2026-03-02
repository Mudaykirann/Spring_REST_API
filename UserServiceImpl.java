package com.example.usercrud.service.impl;

import com.example.usercrud.dto.UserRequestDTO;
import com.example.usercrud.dto.UserResponseDTO;
import com.example.usercrud.entity.User;
import com.example.usercrud.exception.DuplicateEmailException;
import com.example.usercrud.exception.ResourceNotFoundException;
import com.example.usercrud.repository.UserRepository;
import com.example.usercrud.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        log.debug("Creating user with email: {}", requestDTO.getEmail());

        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateEmailException("Email already in use: " + requestDTO.getEmail());
        }

        User user = User.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .build();

        User saved = userRepository.save(user);
        log.info("User created with ID: {}", saved.getId());
        return toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        log.debug("Fetching user with ID: {}", id);
        User user = findUserOrThrow(id);
        return toResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        log.debug("Updating user with ID: {}", id);
        User user = findUserOrThrow(id);

        // Check for email conflict only if email is being changed
        if (!user.getEmail().equalsIgnoreCase(requestDTO.getEmail())
                && userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateEmailException("Email already in use: " + requestDTO.getEmail());
        }

        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());

        User updated = userRepository.save(user);
        log.info("User updated with ID: {}", updated.getId());
        return toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.debug("Deleting user with ID: {}", id);
        User user = findUserOrThrow(id);
        userRepository.delete(user);
        log.info("User deleted with ID: {}", id);
    }

    // ─── Private Helpers ─────────────────────────────────────────────────────────

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    private UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
