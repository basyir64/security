package com.basyir.security.dtos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RoleDto {
    private String name;
    private String description;
    private Set<PermissionDto> permissions = new HashSet<>();
}
