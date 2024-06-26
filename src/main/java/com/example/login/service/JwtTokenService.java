package com.example.login.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtTokenService {
    @Value("${jwt.secret}")
    private String secretKey;
    private long validity = 3600000;// 5 phut(Don vi ms)
    public String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);

        Date now = new Date();
        Date exp = new Date(now.getTime() + validity);
        String accessToken = Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(exp)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();

        return accessToken;
    }
    public String getUserName(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                    .getBody().getSubject();
        } catch (Exception e){
            //do nothing
            e.printStackTrace();
        }
        return null;
    }
}
