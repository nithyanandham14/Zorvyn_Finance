package com.bjsn.finance.Service;

import com.bjsn.finance.Module.Users;
import com.bjsn.finance.repo.Userrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private Userrepo userrepo;
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JWTservice jwTservice;


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    public Users register(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole("VIEWER"); // default role
        }

        return userrepo.save(user);
    }
    public String verify(Users user) {
        Authentication authentication =authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        if(authentication.isAuthenticated())
        {
            return jwTservice.generateToken(user.getUsername());
        }
        return "fail";
    }


}
