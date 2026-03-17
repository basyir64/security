package com.basyir.security.dtos;

import com.basyir.security.entities.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private List<Role> roles;
}
