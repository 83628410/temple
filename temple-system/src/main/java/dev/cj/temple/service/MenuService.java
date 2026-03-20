package dev.cj.temple.service;

import dev.cj.temple.dto.MenuDTO;
import dev.cj.temple.entity.Menu;
import dev.cj.temple.vo.MenuVo;
import dev.cj.temple.vo.RouterVo;

import java.util.List;

public interface MenuService {
    Menu save(MenuDTO menuDTO);
    Menu update(MenuDTO menuDTO);
    void delete(Long id);
    List<Menu> findAll();
    List<MenuVo> findAllVo();
    Menu findById(Long id);
    List<RouterVo> findCurrentRoutes();
    void updateStatus(Long id, Integer status);
}