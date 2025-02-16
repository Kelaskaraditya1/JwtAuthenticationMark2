package com.StarkIndustries.JwtAuthenticationMark2.Service;

import com.StarkIndustries.JwtAuthenticationMark2.Models.PasswordModel;
import com.StarkIndustries.JwtAuthenticationMark2.Models.Users;
import com.StarkIndustries.JwtAuthenticationMark2.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public JwtService jwtService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;


    public String login(Users users){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(),users.getPassword()));
                if(authentication.isAuthenticated()&&userRepository.findByUsername(users.getUsername())!=null)
                    return jwtService.generateJwtToken(users.getUsername());
                else
                    return "false";
    }

    public boolean signup(Users users){


        if(userRepository.findByUsername(users.getUsername())==null){
            users.setPassword(this.bCryptPasswordEncoder.encode(users.getPassword()));
            userRepository.save(users);
            return true;
        }
        return false;
    }

    public List<Users> getUsers(){
        return userRepository.findAll();
    }

    public Users updatePassword(PasswordModel passwordModel){
        Users users = userRepository.findByUsername(passwordModel.getUsername());
        if(users!=null){
            if(this.bCryptPasswordEncoder.matches(passwordModel.getPassword(), users.getPassword())){
                users.setPassword(bCryptPasswordEncoder.encode(passwordModel.getNewPassword()));
                userRepository.save(users);
                return users;
            }
        }
        return null;
    }
}
