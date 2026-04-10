package dev.cj.temple.service;

import dev.cj.temple.dto.UserDTO;
import dev.cj.temple.domain.User;
import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User save(UserDTO userDTO);
    User update(UserDTO userDTO);
    void delete(Long id);
    List<User> findAll();
    User findById(Long id);
}