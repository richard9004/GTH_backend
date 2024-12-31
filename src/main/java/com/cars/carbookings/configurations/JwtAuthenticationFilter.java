package com.cars.carbookings.configurations;

import ch.qos.logback.core.util.StringUtil;
import com.cars.carbookings.services.jwt.UserService;
import com.cars.carbookings.utils.JWTUtil;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserService userService; // Load user information

    // Constructor to initialize dependencies (replacing Lombok's @RequiredArgsConstructor)
    public JwtAuthenticationFilter(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtAuthenticationFilter triggered");

        // Log all request headers
        System.out.println("===== Incoming Request Headers =====");
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            System.out.println(headerName + ": " + request.getHeader(headerName));
        });
        System.out.println("===================================");

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // If no Authorization header or invalid header, skip further processing
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT and user email from the token
        jwt = authHeader.substring(7); // Extract the token
        userEmail = jwtUtil.extractUsername(jwt); // Extract username from token

        try {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
            if (!jwtUtil.isTokenValid(jwt, userDetails)) {
                throw new BadCredentialsException("Invalid or expired token");
            }
        } catch (BadCredentialsException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.getWriter().write("Invalid or expired token");
            response.flushBuffer();
            return;
        } catch (SignatureException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.getWriter().write("JWT signature does not match. Token validation failed.");
            response.flushBuffer();
            return;
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.getWriter().write("Authentication failed due to invalid token.");
            response.flushBuffer();
            return;
        }

        // Print out the extracted user email
        System.out.println("Extracted user email from JWT: " + userEmail);

        if (StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);

            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(usernamePasswordAuthenticationToken);
                SecurityContextHolder.setContext(context);
            }
        }

        filterChain.doFilter(request, response);
    }
}
