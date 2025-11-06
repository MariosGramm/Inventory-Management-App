package com.company.inventory.simple_inventory.dto;

import com.company.inventory.simple_inventory.core.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserReadOnlyDTO {

    private String username;
    private String email;
    private Role role;
    private String firstname;
    private String lastname;

}
