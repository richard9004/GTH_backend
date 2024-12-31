package com.cars.carbookings.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JWTUtil {
    //private static final String SECRET_KEY = "a2V5LXZvLWlzLXZlcnktc2VjdXJlLWFuZC1zdGFibGU=";
 //   private static final String SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//    private Key getSigninkey(){
//        // Generate a secure key for HS256
//        SecretKey secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
//        // Encode the key in Base64
//        String base64EncodedKey = Encoders.BASE64.encode(secretKey.getEncoded());
//        //System.out.println("Generated Secure Key: " + base64EncodedKey);
//        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

//    private Key getSigninkey() {
//        byte[] keyBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
    private SecretKey secretKey;

    public JWTUtil() {
        // Generate the secret key only once when the application starts
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
//
    private Key getSigninkey() {
        return this.secretKey;
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSigninkey()).build().parseClaimsJws(token).getBody();
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimResolvers){
        final Claims claims = extractAllClaims(token);
        return claimResolvers.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

//    public boolean isTokenValid(String token, UserDetails userDetails){
//         final String username = extractUsername(token);
//         return (username.equals(userDetails.getUsername()) && isTokenExpired(token));
//    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private String generateToken(Map<String, Object> extractClaims, UserDetails userDetails){
//         return Jwts.builder().setClaims(extractClaims).setSubject(userDetails.getUsername())
//                 .setIssuedAt(new Date(System.currentTimeMillis()))
//                 .setExpiration(new Date(System.currentTimeMillis()))
//                 .signWith(getSigninkey(), SignatureAlgorithm.HS256).compact();

        // Expiration in 2 minutes (120000 milliseconds)
//        long expirationInMillis = 120000;
//        long expirationInMillis = 15 * 60 * 1000;  // 15 minutes
        long expirationInMillis = 60 * 60 * 1000;  // 1 hour

        // Add roles/authorities to claims
        extractClaims.put("roles", userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority()) // Extract role names
                .toList());

        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) // Token issued time
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMillis)) // Set expiration time
                .signWith(getSigninkey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails){
        return  generateToken(new HashMap<>(), userDetails);
    }

}
