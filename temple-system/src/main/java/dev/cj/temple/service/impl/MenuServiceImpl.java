package dev.cj.temple.service.impl;

import dev.cj.temple.converter.MenuConverter;
import dev.cj.temple.dto.MenuDTO;
import dev.cj.temple.entity.Menu;
import dev.cj.temple.repository.MenuRepository;
import dev.cj.temple.service.MenuService;
import dev.cj.temple.vo.MenuVo;
import dev.cj.temple.vo.RouterVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuConverter menuConverter;
    @Override
    @Transactional
    public Menu save(MenuDTO menuDTO) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public Menu update(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).orElseThrow(() -> new RuntimeException("Menu not found"));
        BeanUtils.copyProperties(menuDTO, menu);
        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }
    /**
     * 查询所有菜单
     */
    @Override
    public List<MenuVo> findAllVo() {
        List<Menu> menuList = menuRepository.findAll();
        return menuList.stream()
                .filter(menu -> "C".equals(menu.getType()))
                .map(menu -> {
                    MenuVo menuVo = menuConverter.toMenuVo(menu);
                    menuVo.setChildren(generateChildrenVo(menu.getId(),menuList));
                    return menuVo;
                })
                .collect(Collectors.toList());
    }
    /**
     * 根据id查询菜单
     */
    @Override
    public Menu findById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }
    /**
     * 查询当前用户菜单
     */
    @Override
    public List<RouterVo> findCurrentRoutes() {
        // 查找type为C和M的菜单
        List<Menu> menus = menuRepository.findAll((root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("orderNum")));
            return criteriaBuilder.and(
                    criteriaBuilder.in(root.get("type")).value(List.of("C", "M")),
                    criteriaBuilder.equal(root.get("status"), 1)
            );
        });
        // 筛选 type 是C的menu,遍历并生成子路由
        List<RouterVo> resultMenu = menus.stream()
                .filter(menu -> "C".equals(menu.getType()))
                .map(menu -> {
                    RouterVo routerVo = menuConverter.toRouterVo(menu);
                    routerVo.setChildren(generateChildrenRoutes(menu.getId(), menus));
                    return routerVo;
                })
                .collect(Collectors.toList());
        // 生成子路由

        return resultMenu;
    }

    /**
     * 生成子路由
     */
    private List<RouterVo> generateChildrenRoutes(Long parentId ,List<Menu> menus) {
        if (parentId == null) {
            return List.of();
        }
        return menus.stream()
                .filter(menu -> "M".equals(menu.getType()))
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> {

                    RouterVo routerVo = menuConverter.toRouterVo(menu);
                    routerVo.setChildren(generateChildrenRoutes(menu.getId(), menus));
                    return routerVo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 生成子菜单
     */
    private List<MenuVo> generateChildrenVo(Long parentId , List<Menu> menuList) {
        if (parentId == null) {
            return List.of();
        }
        return menuList.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> {
                    MenuVo menuVo = menuConverter.toMenuVo(menu);
                    menuVo.setChildren(generateChildrenVo(menu.getId(), menuList));
                    return menuVo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 更新菜单状态
     */
    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new RuntimeException("菜单不存在"));
        menu.setStatus(status);
        menuRepository.save(menu);
    }
}