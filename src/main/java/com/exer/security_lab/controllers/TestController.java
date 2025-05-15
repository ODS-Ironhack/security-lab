package com.exer.security_lab.controllers;

import com.exer.security_lab.models.User;
import com.exer.security_lab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/public")
    public ResponseEntity<String> publicUsers(){
        return ResponseEntity.ok("Ruta pública ");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminRoute(){
        return ResponseEntity.ok("Ruta de admin");
    }

    @GetMapping("/anyrole")
    public ResponseEntity<String> anyRoleRoute(){
        return ResponseEntity.ok("Esto para cualquier role" );
    }

    @GetMapping("/user/{username}")
    public Optional<User> userRoute(@PathVariable String username){
        return userRepository.findByUsername(username);
    }

}
