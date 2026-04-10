package dev.cj.temple.service;

import dev.cj.temple.dto.RoleDTO;
import dev.cj.temple.domain.Role;
import dev.cj.temple.vo.RoleVO;

import java.util.List;

public interface RoleService {
    Role save(RoleDTO roleDTO);
    Role update(RoleDTO roleDTO);
    void delete(Long id);
    List<RoleVO> findAllVO();
    Role findById(Long id);
}