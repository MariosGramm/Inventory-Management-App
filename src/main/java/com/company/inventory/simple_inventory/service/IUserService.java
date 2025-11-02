package com.company.inventory.simple_inventory.service;


import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.dto.UserInsertDTO;
import com.company.inventory.simple_inventory.dto.UserReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.UserUpdateDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IUserService {

    long countUsers();
    LocalDateTime getLastLoginFor(String uuid) throws EntityNotFoundException;
    LocalDateTime getLastLoginByUsername(String username) throws EntityNotFoundException;
    UserReadOnlyDTO addUser(UserInsertDTO insertDTO) throws EntityAlreadyExistsException;
    UserReadOnlyDTO updateUser(String uuid, UserUpdateDTO updateDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
    List<UserReadOnlyDTO> getAllUsers();
    Page<UserReadOnlyDTO> getPaginatedUsers(int page, int size);

}
