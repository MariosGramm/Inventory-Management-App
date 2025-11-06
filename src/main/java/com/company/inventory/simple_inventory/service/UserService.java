package com.company.inventory.simple_inventory.service;

import com.company.inventory.simple_inventory.core.enums.Role;
import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.dto.UserInsertDTO;
import com.company.inventory.simple_inventory.dto.UserReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.UserSearchDTO;
import com.company.inventory.simple_inventory.dto.UserUpdateDTO;
import com.company.inventory.simple_inventory.mapper.Mapper;
import com.company.inventory.simple_inventory.model.User;
import com.company.inventory.simple_inventory.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final Mapper mapper;


    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public void deleteUser(String uuid) { userRepository.deleteByUuid(uuid);}

    @Override
    @Transactional(rollbackOn = EntityNotFoundException.class)
    public LocalDateTime getLastLoginFor(String uuid) throws EntityNotFoundException {
        try {
            User user = userRepository.findByUuid(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("User", "User does not exist"));

            return user.getLastLogin();
        }catch (EntityNotFoundException e){
            log.error("Unable to retrieve last login for user with uuid = {}. {}", uuid, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = EntityNotFoundException.class)
    public LocalDateTime getLastLoginByUsername(String username) throws EntityNotFoundException {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User","User does not exist"));

            return user.getLastLogin();
        }catch (EntityNotFoundException e) {
            log.error("Unable to retrieve last login for user with username = {} . {}", username, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = EntityAlreadyExistsException.class)
    public UserReadOnlyDTO addUser(UserInsertDTO insertDTO) throws EntityAlreadyExistsException {
        try {

            if (userRepository.existsByEmail(insertDTO.getEmail())) {
                log.warn("Attempt to register user with existing email: {}", insertDTO.getEmail());
                throw new EntityAlreadyExistsException("User", "A user with this email already exists");
            }


            if (userRepository.existsByUsername(insertDTO.getUsername())) {
                log.warn("Attempt to register user with existing username: {}", insertDTO.getUsername());
                throw new EntityAlreadyExistsException("User", "A user with this username already exists");
            }


            User user = mapper.mapToUser(insertDTO);


            if (user.getRole() == null) {
                user.setRole(Role.USER); // default ρόλος αν δεν έχει οριστεί
            }
            user.setDeleted(false);


            userRepository.save(user);
            log.info("User saved successfully with email = {}", user.getEmail());


            return mapper.mapToUserReadOnlyDTO(user);

        } catch (EntityAlreadyExistsException e) {
            log.error("Save failed for user with email = {}. User already exists", insertDTO.getEmail());
            throw e;
        }
    }


    @Override
    @Transactional(rollbackOn ={EntityNotFoundException.class,EntityAlreadyExistsException.class})
    public UserReadOnlyDTO updateUser(String uuid, UserUpdateDTO updateDTO) throws EntityAlreadyExistsException, EntityNotFoundException {
        try {
            User user = userRepository.findByUuid(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("User", "User does not exist"));


            Optional<User> existingUserOpt = userRepository.findByUsername(updateDTO.getUsername());
            if (existingUserOpt.isPresent() && !existingUserOpt.get().getUuid().equals(uuid)) {
                throw new EntityAlreadyExistsException("User", "User already exists");
            }

            user.setFirstname(updateDTO.getFirstname());
            user.setLastname(updateDTO.getLastname());
            user.setRole(updateDTO.getRole());
            user.setUsername(updateDTO.getUsername());

            userRepository.save(user);
            return mapper.mapToUserReadOnlyDTO(user);

        } catch (EntityNotFoundException e) {
            log.error("User with uuid = {} does not exist.", uuid);
            throw e;
        } catch (EntityAlreadyExistsException e) {
            log.error("User with uuid = {} not updated. {}", uuid, e.getMessage());
            throw e;
        }
    }


    @Override
    public List<UserReadOnlyDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return List.copyOf(users.stream().map(mapper::mapToUserReadOnlyDTO).toList());
    }

    @Override
    public Page<UserReadOnlyDTO> getPaginatedUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);
        log.debug("Page = {} , Size = {}", page,size);
        return userPage.map(mapper::mapToUserReadOnlyDTO);
    }

    @Override
    public Page<UserReadOnlyDTO> getPaginatedNotDeletedUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<User> userPage = userRepository.findByDeletedFalse(pageable);
        return userPage.map(mapper::mapToUserReadOnlyDTO);
    }

    @Override
    @Transactional(rollbackOn = EntityNotFoundException.class)
    public List<UserReadOnlyDTO> searchUsers(UserSearchDTO dto) throws EntityNotFoundException {

        List<User> users = userRepository.searchByFilters(
                dto.getKeyword(),
                dto.getRole(),
                dto.isShowDeleted()
        );

        if (users.isEmpty()) {
            throw new EntityNotFoundException("User", "No users found with the given filters.");
        }

        return users.stream()
                .map(mapper::mapToUserReadOnlyDTO)
                .toList();
    }
}



