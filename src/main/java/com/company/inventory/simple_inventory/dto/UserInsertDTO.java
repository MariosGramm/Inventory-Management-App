package com.company.inventory.simple_inventory.dto;

import com.company.inventory.simple_inventory.core.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInsertDTO {

    @NotBlank(message = "Name field cannot be blank")
    private String username;

    private String firstname;

    private String lastname;

    @NotBlank(message = "Email field cannot be blank")
    private String email;

    @NotBlank(message = "Password field cannot be blank")
    private String password;

    @NotBlank(message = "Please choose a role")
    private Role role;
}
