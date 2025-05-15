package com.exer.security_lab.service;


import com.exer.security_lab.models.ERole;
import com.exer.security_lab.models.Role;
import com.exer.security_lab.models.User;
import com.exer.security_lab.repositories.UserRepository;
import com.exer.security_lab.repositories.RoleRepository;
import com.exer.security_lab.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    private User user;
    private Role rol;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

 //   @BeforeEach
/*    public void setUp() {
        rol = new Role();
        rol.setName(ERole.ROLE_ADMIN);

        user = new User();
        user.setUsername("testing");
        user.setPassword("1234");
        user.setRoles((Collection<Role>) rol);
        System.out.println("El usuario inicial es: " + user);

        userService.saveUser(user);
    }*/

    @Test
    public void createUserTest() {
        Role rol = new Role();
        rol.setName(ERole.ROLE_ADMIN);
        roleRepository.save(rol);

        User user = new User();
        user.setUsername("User Admin");
        user.setPassword("1234");
        user.setRole(List.of(rol));

        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser, "El usuario no se guardó correctamente.");

        System.out.println("Usuario guardado: " + savedUser);

    }}
/*

    @AfterEach
    public void tearDown() {
        userRepository.delete(user);
    }
*/
/*

    @Test
    @DisplayName("La encriptación de passwords es correcta")
    public void passwordEncryption() {
        assertTrue(user.getPassword().startsWith("$2a$"));
        System.out.println("este es el user final: " + user);
    }
}*/
