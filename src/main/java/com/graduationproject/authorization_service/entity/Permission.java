package com.graduationproject.authorization_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions")
public class Permission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    private String description;

    @Column(nullable = false)
    private String module;

    @Column(nullable = false)
    private String action;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnoreProperties("permissions")
    private Set<Role> roles = new HashSet<>();
}
