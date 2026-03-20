package dev.cj.temple.service;

import dev.cj.temple.dto.UserDTO;
import dev.cj.temple.entity.User;
import dev.cj.temple.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    public void testSaveUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("123456");
        userDTO.setNickname("测试用户");
        userDTO.setEmail("test@example.com");
        userDTO.setPhone("13800138000");
        userDTO.setStatus(1);
        
        User user = userService.save(userDTO);
        assertNotNull(user);
        assertNotNull(user.getId());
    }
    
    @Test
    public void testFindByUsername() {
        User user = userService.findByUsername("testuser");
        assertNotNull(user);
    }
}