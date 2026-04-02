package com.bjsn.finance.Service;

import com.bjsn.finance.Module.Users;
import com.bjsn.finance.repo.Userrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private Userrepo userrepo;

    @Override
    public UserDetails loadUserByUsername(String UserName) throws UsernameNotFoundException {
        Users user = userrepo.findByUsername(UserName);
        System.out.println("checking..." + user.toString());
        if(user == null) {
            System.out.println("USer not found ");
            throw new UsernameNotFoundException("User not found ");

        }
        return new User (
            user.getUsername(),
            user.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole())
                )
        );


    }
}
