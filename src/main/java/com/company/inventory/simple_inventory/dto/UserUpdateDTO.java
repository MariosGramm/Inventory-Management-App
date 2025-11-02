package com.company.inventory.simple_inventory.dto;

import com.company.inventory.simple_inventory.core.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserUpdateDTO {

    @NotBlank(message = "Name field cannot be blank")
    private String username;

    @NotBlank(message = "Password field cannot be blank")
    private String password;

    private String firstname;

    private String lastname;

    @NotBlank(message = "Please choose a role")
    private Role role;
}

