package com.agri.platform;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.agri.platform.DTO.UserRegisterDTO;
import com.agri.platform.entity.User;
import com.agri.platform.mapper.UserMapper;
import com.agri.platform.enums.UserLoginType;
import com.agri.platform.exception.BizException;
import com.agri.platform.service.UserRegisterService;

@ExtendWith(MockitoExtension.class)
public class RegisterTests {
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserRegisterService userRegisterService;

    @Test
    void testNormalRegister() {
        UserRegisterDTO byUsername = new UserRegisterDTO(UserLoginType.USERNAME, "testuser", "password123");

        User byU = User.builder()
        .userId("UUID-0001")
        .username("testuser")
        .passwordHash("1234")
        .build();

        User byE = User.builder()
        .userId("UUID-0002")
        .email("1983517529@qq.com")
        .passwordHash("1234")
        .build();

        User byP = User.builder()
        .userId("UUID-0003")
        .phoneNumber("13517529835")
        .passwordHash("1234")
        .build();

        UserRegisterDTO byEmail = new UserRegisterDTO(UserLoginType.EMAIL, "1983517529@qq.com", "password123");

        UserRegisterDTO byPhoneNumber = new UserRegisterDTO(UserLoginType.PHONE_NUMBER, "13517529835", "password123");

        when(userMapper.insertUser(byU)).thenReturn(1);
        when(userMapper.insertUser(byE)).thenReturn(1);
        when(userMapper.insertUser(byP)).thenReturn(1);

        assertDoesNotThrow(() -> userRegisterService.registerUser(byUsername));
        assertDoesNotThrow(() -> userRegisterService.registerUser(byEmail));
        assertDoesNotThrow(() -> userRegisterService.registerUser(byPhoneNumber));
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
