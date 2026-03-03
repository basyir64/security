package com.basyir.security.core;

import com.basyir.security.entities.User;
import com.basyir.security.exceptions.MissingJWTException;
import com.basyir.security.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JWT_Auth_Filter extends OncePerRequestFilter {

    private final JWT_Service jwtService;
    private final UserService userService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String requestTokenHeader = request.getHeader("Authorization");

            // Requests where auth header is not included
            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {

                // Whitelist
                boolean isLoginRequest = (contextPath + "/auth/login").equals(request.getRequestURI());
                boolean isSignUpRequest = (contextPath + "/auth/signup").equals(request.getRequestURI());
                boolean isRefreshRequest = (contextPath + "/auth/refresh").equals(request.getRequestURI());

                if (isLoginRequest || isSignUpRequest || isRefreshRequest) {
                    filterChain.doFilter(request, response);
                    return;
                }

                // Otherwise, require auth
                throw new MissingJWTException("Please include Authorization header with Bearer token");
            }

            String token = requestTokenHeader.split("Bearer ")[1];
            Long userId = jwtService.getUserIdFromToken(token);

            // to debug here, send non-auth requests
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getUserById(userId);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) { //Various JWT Exceptions are caught by SecurityGlobalExceptionHandler
            log.error(e.getMessage());
            handlerExceptionResolver.resolveException(request,response,null,e);
        }
    }
}
