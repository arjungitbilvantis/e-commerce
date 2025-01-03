package com.bilvantis.ecommerce.config;

import com.bilvantis.ecommerce.api.exception.ErrorResponse;
import com.bilvantis.ecommerce.api.util.JwtUtil;
import com.bilvantis.ecommerce.model.UserResponseDTO;
import com.bilvantis.ecommerce.util.ECommerceAppConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    private static final List<String> EXCLUDED_PATHS = Arrays.asList("/login", "/login/one-time-password", "/user");

    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming HTTP requests to check for JWT authentication and handles expired or invalid tokens.
     *
     * @param request     the HttpServletRequest object
     * @param response    the HttpServletResponse object
     * @param filterChain the FilterChain object
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader(ECommerceAppConstant.AUTHORIZATION_HEADER);

        String phoneNumber = null;
        String jwt = null;

        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith(ECommerceAppConstant.BEARER_PREFIX)) {
            jwt = authorizationHeader.substring(7);
            try {
                phoneNumber = jwtUtil.extractUsername(jwt); // This might throw ExpiredJwtException
            } catch (ExpiredJwtException e) {
                // Handle expired token
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token has expired. Please login again.", ECommerceAppConstant.TOKEN_EXPIRED_CODE);
                return;
            } catch (Exception e) {
                // Handle other JWT processing errors
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(ECommerceAppConstant.INVALID_OR_EXPIRED_TOKEN);
                return;
            }
        }

        if (Objects.nonNull(phoneNumber) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(phoneNumber);

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (UsernameNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(ECommerceAppConstant.INVALID_OR_EXPIRED_TOKEN);
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(ECommerceAppConstant.TOKEN_PROCESSING_ERROR);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Helper method to send a standardized error response.
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String message, String fieldId) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        // Construct the error response
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        errorResponse.setFieldId(fieldId);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setBody(null);
        userResponseDTO.setStatus(ECommerceAppConstant.ERROR);
        userResponseDTO.setErrors(Collections.singletonList(errorResponse));

        // Convert the response to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(userResponseDTO);

        response.getWriter().write(jsonResponse);
    }

    /**
     * Determines whether the request should be filtered or not based on the request URI.
     *
     * @param request the HttpServletRequest object
     * @return true if the request URI is in the list of excluded paths and should not be filtered, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return EXCLUDED_PATHS.contains(path);
    }
}




