package dev.cj.temple.service.impl;

import dev.cj.temple.dto.UserDTO;
import dev.cj.temple.entity.Role;
import dev.cj.temple.entity.User;
import dev.cj.temple.repository.RoleRepository;
import dev.cj.temple.repository.UserRepository;
import dev.cj.temple.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    @Transactional
    public User save(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        
        if (userDTO.getRoleIds() != null) {
            List<Role> roles = roleRepository.findAllById(List.of(userDTO.getRoleIds()));
            user.setRoles(roles);
        }
        
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public User update(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        BeanUtils.copyProperties(userDTO, user, "password");
        
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        if (userDTO.getRoleIds() != null) {
            List<Role> roles = roleRepository.findAllById(List.of(userDTO.getRoleIds()));
            user.setRoles(roles);
        }
        
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}