package com.example.authtry;


import com.example.authtry.model.ApplicationUser;
import com.example.authtry.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        ApplicationUser user = userRepository.findByUsername(s);
        System.out.println(user.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException(s);
        }
        return new User(user.getUsername(),user.getPassword(),new ArrayList<>());
    }
}