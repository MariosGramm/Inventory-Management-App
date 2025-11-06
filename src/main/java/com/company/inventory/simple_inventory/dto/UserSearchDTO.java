package com.company.inventory.simple_inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDTO {

    private String keyword;      // username or email
    private String role;
    private boolean showDeleted; // show soft deleted or not
}
