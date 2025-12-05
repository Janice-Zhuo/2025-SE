package com.agri.platform;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.agri.platform.DTO.UserRegisterDTO;
import com.agri.platform.entity.User;
import com.agri.platform.enums.UserLoginType;
import com.agri.platform.exception.BizException;
import com.agri.platform.service.UserRegisterService;

@SpringBootTest
public class RegisterTests {
    @Autowired
    private UserRegisterService userRegisterService;

    @Test
    void testNormalRegister() {
        // Add test cases for user registration here
        UserRegisterDTO byUsername = new UserRegisterDTO(UserLoginType.USERNAME, "testuser", "password123");

        UserRegisterDTO byEmail = new UserRegisterDTO(UserLoginType.EMAIL, "1983517529@qq.com", "password123");

        UserRegisterDTO byPhoneNumber = new UserRegisterDTO(UserLoginType.PHONE_NUMBER, "13517529835", "password123");

        User user = userRegisterService.registerUser(byUsername);

        User user2 = userRegisterService.registerUser(byEmail);

        User user3 = userRegisterService.registerUser(byPhoneNumber);
    }

    @Test
    void testMissingRegister() {
        UserRegisterDTO missingLogin = new UserRegisterDTO(UserLoginType.USERNAME, "", "password123");

        UserRegisterDTO missingPassword = new UserRegisterDTO(UserLoginType.USERNAME, "testuser", "");

        BizException ex1 = assertThrows(BizException.class, () -> {
            userRegisterService.registerUser(missingLogin);
        });

        assertTrue(ex1.getMessage().contains("至少"));

        BizException ex2 = assertThrows(BizException.class, () -> {
            userRegisterService.registerUser(missingPassword);
        });

        assertTrue(ex2.getMessage().contains("不能为空"));
    }
    
    @Test
    void testDuplicateRegister() {
        UserRegisterDTO dto = new UserRegisterDTO(UserLoginType.USERNAME, "duplicateUser", "password123");

        userRegisterService.registerUser(dto);

        BizException ex = assertThrows(BizException.class, () -> {
            userRegisterService.registerUser(dto);
        });

        assertTrue(ex.getMessage().contains("已存在"));
    }
}
