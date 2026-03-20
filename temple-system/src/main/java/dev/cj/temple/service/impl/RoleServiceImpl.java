package dev.cj.temple.service.impl;

import dev.cj.temple.dto.RoleDTO;
import dev.cj.temple.entity.Menu;
import dev.cj.temple.entity.Role;
import dev.cj.temple.repository.MenuRepository;
import dev.cj.temple.repository.RoleRepository;
import dev.cj.temple.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    
    @Override
    @Transactional
    public Role save(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        
        if (roleDTO.getMenuIds() != null) {
            List<Menu> menus = menuRepository.findAllById(List.of(roleDTO.getMenuIds()));
            role.setMenus(menus);
        }
        
        return roleRepository.save(role);
    }
    
    @Override
    @Transactional
    public Role update(RoleDTO roleDTO) {
        Role role = roleRepository.findById(roleDTO.getId()).orElseThrow(() -> new RuntimeException("Role not found"));
        BeanUtils.copyProperties(roleDTO, role);
        
        if (roleDTO.getMenuIds() != null) {
            List<Menu> menus = menuRepository.findAllById(List.of(roleDTO.getMenuIds()));
            role.setMenus(menus);
        }
        
        return roleRepository.save(role);
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
    
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    
    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
}