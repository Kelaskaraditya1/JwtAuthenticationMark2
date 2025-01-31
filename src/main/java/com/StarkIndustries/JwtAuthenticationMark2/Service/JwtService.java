package com.StarkIndustries.JwtAuthenticationMark2.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {

    Logger logger = LoggerFactory.getLogger(JwtService.class);

    private String secreteKey="";

    public JwtService(){
        try{
            KeyGenerator keyGenerator =KeyGenerator.getInstance("HmacSha256");
            SecretKey sK  = keyGenerator.generateKey();
            secreteKey=Base64.getEncoder().encodeToString(sK.getEncoded());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String generateJwtToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+(60000))) // valid for 1 sec
                .and()
                .signWith(getKey())
                .compact();
    }

    public SecretKey getKey(){
        byte[] byteKey = Decoders.BASE64.decode(secreteKey);
        return Keys.hmacShaKeyFor(byteKey);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    public  <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        logger.debug("username",String.valueOf(userName.equals(userDetails.getUsername())));
        logger.debug("status",String.valueOf(!isTokenExpired(token)));
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
