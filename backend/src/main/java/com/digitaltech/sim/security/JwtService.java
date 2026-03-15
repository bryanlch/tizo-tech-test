package com.digitaltech.sim.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Base de generacion y parseo JWT.
 */
@Service
public class JwtService {

    /**
     * Llave de 256 bits generada dinamicamente en memoria.
     * Para produccion usar persistente y larga.
     */
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Validez genérica para el token: 10 horas.
     */
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    /**
     * Obtiene el usuario del token
     * @param token el JWT token
     * @return username contenido en JWT
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae un claim con map de la payload.
     * @param token jwtt
     * @param claimsResolver extractor generico
     * @param <T> tipado dinamico
     * @return el campo resultante
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Genera token simple.
     * @param userDetails objeto spring details
     * @return String de token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera token con extra claims.
     * @param extraClaims map custom
     * @param userDetails string object principal
     * @return String de token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Comprueba si el token pertenece al usuario y si esta valido aun.
     * @param token string puro
     * @param userDetails string del principal spring request
     * @return verdadero si match correcto
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
