package com.basyir.security.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
public class RoleDto {
    private String name;
    private String description;
    private List<PermissionDto> permissions = new ArrayList<>();
}
