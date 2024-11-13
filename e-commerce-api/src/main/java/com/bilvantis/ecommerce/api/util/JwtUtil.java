package com.bilvantis.ecommerce.api.util;

import com.bilvantis.ecommerce.dao.data.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // The SECRET can be any string, but we'll generate a secure key for HS512
    private static final String SECRET = "4027d080f9a3954189ebca5ddbbfdfc9cb824314c0d6c910eff8bec1c689eabc";

    // Generate the Key using the correct size (HS512 requires 512 bits or 64 bytes)
    private Key getSignKey() {
        // Generate a 512-bit key for HS512 signing
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Generates a JWT token for the given user.
     * <p>
     * This method creates a JWT token using the user's phone number as the subject and includes
     * additional claims such as user ID, phone number, email, first name, and last name. The token
     * is set to expire in 1 hour from the time of creation and is signed using the HS512 algorithm.
     *
     * @param user the user for whom the token is being generated
     * @return the generated JWT token as a String
     */
    public String generateToken(User user) {
        // Create JWT token based on the phone number
        return Jwts.builder()
                .setSubject(user.getPhoneNumber())  // Use phone number as subject
                .claim("user_id", user.getUserId())
                .claim("phone_number", user.getPhoneNumber())
                .claim("email", user.getEmail())
                .claim("first_name", user.getFirstName())
                .claim("last_name", user.getLastName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))  // 1 hour
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token the JWT token from which to extract the username
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    /**
     * Validates the given JWT token against the provided UserDetails.
     *
     * @param token       the JWT token to validate
     * @param userDetails the UserDetails to validate the token against
     * @return true if the token is valid and not expired, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token the JWT token to check for expiration
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

}
