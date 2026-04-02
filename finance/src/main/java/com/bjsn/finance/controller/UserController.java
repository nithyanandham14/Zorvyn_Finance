package com.bjsn.finance.controller;

import com.bjsn.finance.Module.Users;
import com.bjsn.finance.Service.JWTservice;
import com.bjsn.finance.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTservice jwTservice;
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @PostMapping("/register")
    public Users adduser(@RequestBody Users user)
    {
        return userService.register(user);
    }
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user)
    {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Invalid username or password"));
        }
        String token=jwTservice.generateToken(user.getUsername());
        return ResponseEntity.ok(token);
    }
}
