//package com.labweek.menumate.config;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//    private final UserAuthProvider userAuthProvider;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain) throws ServletException, IOException {
//
//        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if(header != null){
//            String[] elements = header.split(" ");
//
//            if(elements.length==2 && "Bearer".equals(elements[0])){
//                try{
//                    SecurityContextHolder.getContext().setAuthentication(
//                            userAuthProvider.validateToken(elements[1])
//                    );
//                }catch (RuntimeException e){
//                    SecurityContextHolder.clearContext();
//                    throw e;
//                }
//            }
//
//            try {
//                // Verify and decode the token
//                DecodedJWT decodedJWT = verifyToken(token);
//                // If token is valid, continue with the request
//                filterChain.doFilter(request, response);
//            } catch (JWTVerificationException e) {
//                // Token is expired or invalid
//                response.setStatus(401);
//                response.getWriter().write("Unauthorized: Token has expired or is invalid.");
//            }
//        }
//
//        filterChain.doFilter(request,response);
//
//
//    }
//    // Method to verify the JWT token
//    private DecodedJWT verifyToken(String token) throws JWTVerificationException {
//        JWTVerifier verifier = JWT.require(com.auth0.jwt.algorithms.Algorithm.HMAC256(userAuthProvider.secretKey))  // Use your secret key here
//                .build();
//        return verifier.verify(token);  // This will throw exception if token is expired or invalid
//    }
//
//}

package com.labweek.menumate.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserAuthProvider userAuthProvider; // Assuming this is your custom provider

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Check if the Authorization header is present
        if (header != null && header.startsWith("Bearer ")) {
            // Extract the token from the header
            String token = header.substring(7); // Remove "Bearer " prefix

            try {
                // Validate the token using your provider method (which returns Authentication)
                Authentication authentication = userAuthProvider.validateToken(token);

                // Set authentication context if token is valid
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (JWTVerificationException e) {
                // If token is invalid or expired, clear the context and send 401 Unauthorized
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Token is invalid or expired.");
                return;  // Don't proceed further if token is invalid
            }
        }

        // Continue with the filter chain (allow the request to proceed)
        filterChain.doFilter(request, response);
    }
}