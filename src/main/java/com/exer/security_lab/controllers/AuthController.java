package com.exer.security_lab.controllers;

import com.exer.security_lab.models.ERole;
import com.exer.security_lab.models.User;
import com.exer.security_lab.services.JwtService;
import com.exer.security_lab.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> optionalUser = userService.getByUserName(user.getUsername());

        if (optionalUser.isPresent()) {

            User foundUser = optionalUser.get();

            if (userService.passwordIsValid(foundUser, user.getPassword())) {
                System.out.println(foundUser);
                System.out.println(foundUser.getRole());
                List<ERole> roleNames = foundUser.getRole().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toList());
                System.out.println(roleNames);
                // una vez vemos que el usuario es correcto, generamos el token
                String token = jwtService.generateToken(foundUser.getUsername(), roleNames.toString());

                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect login");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
