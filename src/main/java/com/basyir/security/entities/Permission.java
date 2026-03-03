package com.basyir.security.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "app_permission")
@NoArgsConstructor
public class Permission {

    public Permission(String name) {
        this.name = name;
    }

    public Permission(Long id, String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "permissions")
    @JsonBackReference  // This side will be omitted during serialization
    private Set<Role> roles = new HashSet<>();
}
