package com.StarkIndustries.JwtAuthenticationMark2.Service;

import com.StarkIndustries.JwtAuthenticationMark2.Models.UserPrinciples;
import com.StarkIndustries.JwtAuthenticationMark2.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserPrinciples(userRepository.findByUsername(username));
    }
}
