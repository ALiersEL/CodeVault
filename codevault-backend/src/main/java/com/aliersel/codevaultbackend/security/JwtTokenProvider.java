package com.aliersel.codevaultbackend.security;

import com.aliersel.codevaultbackend.constant.enums.AppUserRole;
import com.aliersel.codevaultbackend.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    /**
     * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
     * microservices environment, this key would be kept on a config-server.
     */
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds;

    @Autowired
    private MyUserDetails myUserDetails;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, Integer userid, List<AppUserRole> appUserRoles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("userid", userid);
        claims.put("auth", appUserRoles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime validity = now.plusSeconds(validityInMilliseconds / 1000);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(Date.from(now.toInstant()))//
                .setExpiration(Date.from(validity.toInstant()))//
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))//
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Integer getUserid(String token) {
        return (Integer) Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))).build().parseClaimsJws(token).getBody().get("userid");
    }

    public OffsetDateTime getExpireTime(String token) {
        return OffsetDateTime.ofInstant(Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))).build().parseClaimsJws(token).getBody().getExpiration().toInstant(), OffsetDateTime.now().getOffset());
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.split(" ")[1].trim();
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
//            System.out.println(e.getMessage());
            throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}