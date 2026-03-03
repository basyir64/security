package com.basyir.security.controllers;

import com.basyir.security.dtos.RoleDto;
import com.basyir.security.entities.Role;
import com.basyir.security.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    
    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasAuthority('roles:create')")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.save(roleDto), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('roles:read')")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('roles:read')")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Long id) {
        return new ResponseEntity<>(roleService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('roles:update')")
    public ResponseEntity<RoleDto> updateRoleById(@PathVariable Long id, @RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.updateById(id, roleDto), HttpStatus.OK);
    }
}
