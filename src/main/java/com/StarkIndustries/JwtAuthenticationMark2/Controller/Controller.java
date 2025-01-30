package com.StarkIndustries.JwtAuthenticationMark2.Controller;

import com.StarkIndustries.JwtAuthenticationMark2.Models.Users;
import com.StarkIndustries.JwtAuthenticationMark2.Service.JwtService;
import com.StarkIndustries.JwtAuthenticationMark2.Service.MyUserDetailsService;
import com.StarkIndustries.JwtAuthenticationMark2.Service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    public UserService userService;

    @Autowired
    public JwtService jwtService;

    @Autowired
    public MyUserDetailsService myUserDetailsService;

    @GetMapping("/greetings")
    public ResponseEntity<String> greetings(){
        return ResponseEntity.status(HttpStatus.OK).body("Greetings\nI am Optimus Prime");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody Users users){
        if(userService.login(users)!="false")
            return ResponseEntity.status(HttpStatus.OK).body(userService.login(users));
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Un-Authenticated!!");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody Users users){
        if(userService.signup(users))
            return ResponseEntity.status(HttpStatus.CREATED).body("Signp Successfully!!");
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Signup!!");
    }

//    @GetMapping("/validate")
//    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader){
//        if(authHeader!=null && authHeader.startsWith("Bearer ")){
//            String token = authHeader.substring(7);
//            String username = jwtService.extractUserName(token);
//            UserDetails userDetails = new MyUserDetailsService().loadUserByUsername(username);
//            boolean status= jwtService.validateToken(token,userDetails);
//            if(status)
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body("User Login Successfully!!");
//            else
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to Login!!");
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to Login!!");
//    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateJwtToken(@RequestHeader("Authorization") String token) {
        try {
            System.out.println("Received token: " + token);  // Log the token for debugging
            String username = jwtService.extractUserName(token);
            System.out.println("Extracted username: " + username);

            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

            if (!jwtService.validateToken(token, userDetails)) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
            }

            Claims claims = jwtService.extractAllClaims(token);
            return ResponseEntity.ok(Map.of(
                    "message", "Token is valid",
                    "username", claims.getSubject(),
                    "issuedAt", claims.getIssuedAt(),
                    "expiresAt", claims.getExpiration()
            ));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());  // Log error message
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getUsers(){
        if(!userService.getUsers().isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
