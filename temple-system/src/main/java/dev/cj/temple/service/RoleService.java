package dev.cj.temple.service;

import dev.cj.temple.dto.RoleDTO;
import dev.cj.temple.entity.Role;
import java.util.List;

public interface RoleService {
    Role save(RoleDTO roleDTO);
    Role update(RoleDTO roleDTO);
    void delete(Long id);
    List<Role> findAll();
    Role findById(Long id);
}