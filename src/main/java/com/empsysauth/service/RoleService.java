package com.empsysauth.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.empsysauth.dto.RoleDto;
import com.empsysauth.entity.Role;
import com.empsysauth.repository.RoleRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;


    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }


    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return toDto(role);
    }


    public RoleDto createRole(RoleDto roleDto) {
        if (roleRepository.existsByRoleName(roleDto.getName())) {
            throw new RuntimeException("Role with name '" + roleDto.getName() + "' already exists");
        }

        Role role = new Role();
        role.setRoleName(roleDto.getName());
        Role saved = roleRepository.save(role);
        return toDto(saved);
    }


    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        if (!role.getRoleName().equals(roleDto.getName()) &&
                roleRepository.existsByRoleName(roleDto.getName())) {
            throw new RuntimeException("Role with name '" + roleDto.getName() + "' already exists");
        }

        role.setRoleName(roleDto.getName());
        Role updated = roleRepository.save(role);
        return toDto(updated);
    }


    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    private RoleDto toDto(Role role) {
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setName(role.getRoleName());
        return dto;
    }
}
