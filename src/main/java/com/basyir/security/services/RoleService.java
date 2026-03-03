package com.basyir.security.services;

import com.basyir.security.dtos.PermissionDto;
import com.basyir.security.dtos.RoleDto;
import com.basyir.security.entities.Permission;
import com.basyir.security.entities.Role;
import com.basyir.security.exceptions.ResourceNotFoundException;
import com.basyir.security.repositories.PermissionRepository;
import com.basyir.security.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    public RoleDto save(RoleDto roleDto) {
        Set<Permission> permissions = new HashSet<>();
        Role role = modelMapper.map(roleDto, Role.class);
        for (PermissionDto permissionDto : roleDto.getPermissions()) {
            Long permissionDtoId = permissionDto.getId();
            Permission newPermission = permissionRepository.findById(permissionDtoId).orElseThrow(() -> new ResourceNotFoundException("Permission not found by id: " + permissionDtoId));
            permissions.add(newPermission);
        }
        role.setPermissions(permissions);
        roleRepository.save(role);
        return modelMapper.map(role, RoleDto.class);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public RoleDto findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found by id: " + id));
        return modelMapper.map(role, RoleDto.class);
    }

    public RoleDto updateById(Long id, RoleDto roleDto) {

        Role roleToUpdate = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found by id: " + id));
        roleToUpdate.setName(roleDto.getName());
        roleToUpdate.setDescription(roleDto.getDescription());

        Set<Permission> newPermissions = new HashSet<>();
        for (PermissionDto permissionDto : roleDto.getPermissions()) {
            Long permissionDtoId = permissionDto.getId();
            Permission newPermission = permissionRepository.findById(permissionDtoId).orElseThrow(() -> new ResourceNotFoundException("Permission not found by id: " + permissionDtoId));
            newPermissions.add(newPermission);
        }
        roleToUpdate.getPermissions().clear();
        roleToUpdate.getPermissions().addAll(newPermissions);
        roleRepository.save(roleToUpdate);

        return modelMapper.map(roleToUpdate, RoleDto.class);
    }
}
