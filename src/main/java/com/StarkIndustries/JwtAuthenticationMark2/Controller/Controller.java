package com.StarkIndustries.JwtAuthenticationMark2.Controller;

import com.StarkIndustries.JwtAuthenticationMark2.Keys.Keys;
import com.StarkIndustries.JwtAuthenticationMark2.Models.PasswordModel;
import com.StarkIndustries.JwtAuthenticationMark2.Models.Users;
import com.StarkIndustries.JwtAuthenticationMark2.Service.EmailService;
import com.StarkIndustries.JwtAuthenticationMark2.Service.JwtService;
import com.StarkIndustries.JwtAuthenticationMark2.Service.MyUserDetailsService;
import com.StarkIndustries.JwtAuthenticationMark2.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
import java.security.SignatureException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    public UserService userService;

    @Autowired
    public JwtService jwtService;

    @Autowired
    public MyUserDetailsService myUserDetailsService;

    @Autowired
    public EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping("/greetings")
    public ResponseEntity<String> greetings(){
        logger.debug("Greetings");
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

    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestParam("Authorization") String jwtToken){
        if(!jwtToken.isEmpty() && jwtToken.startsWith("Bearer ")){
            String token = jwtToken.substring(7);
            String username = jwtService.extractUserName(token);
            UserDetails userDetails = new MyUserDetailsService().loadUserByUsername(username);
            if(jwtService.validateToken(token,userDetails))
                return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getUsers(){
        if(!userService.getUsers().isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/update-password")
    public ResponseEntity<Users> updatePassword(@RequestBody PasswordModel passwordModel){
        Users users = userService.updatePassword(passwordModel);
        System.out.println(passwordModel);
        if(users!=null)
            return ResponseEntity.status(HttpStatus.OK).body(users);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/verify-email/{email}")
    public ResponseEntity<String> sendEmailVerificationOtp(@PathVariable String email,HttpSession session){
        Random random = new Random();
        int otp=1000+random.nextInt(9000);
        session.setAttribute(Keys.OTP,otp);
        session.setMaxInactiveInterval(300);
        if(emailService.sendEmail(otp,email))
            return ResponseEntity.status(HttpStatus.OK).body("Email sent Successfully!!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sent email!!");
    }

    @GetMapping("/verify-email-with-otp/{otp}")
    public ResponseEntity<String> verifyEmail(@PathVariable int otp,HttpSession session){
        int generatedOtp = Integer.parseInt(session.getAttribute(Keys.OTP).toString());
        System.out.println(generatedOtp);
        if(generatedOtp==otp){
            session.removeAttribute(Keys.OTP);
            return ResponseEntity.status(HttpStatus.OK).body("Email verified Successfully!!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/get-both")
    public ResponseEntity<?> getBoth(@RequestParam("user") String user, @RequestParam("image")MultipartFile multipartFile){

        try{
            Users users = this.objectMapper.readValue(user,Users.class) ;
            logger.info("user:{}",users);
            logger.info("file name: {}", multipartFile.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.OK).body(users+" "+multipartFile.getOriginalFilename());
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
