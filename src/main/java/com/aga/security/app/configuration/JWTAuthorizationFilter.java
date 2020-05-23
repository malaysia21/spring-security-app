package com.aga.security.app.configuration;

import com.aga.security.app.model.UserEntity;
import com.aga.security.app.model.UserEntityToUserDetails;
import com.aga.security.app.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter
{
    private UserRepository userRepository;
    private UserEntityToUserDetails userEntityToUserDetails;


    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, UserEntityToUserDetails userEntityToUserDetails) {
        super(authenticationManager);
        this.userRepository  = userRepository;
        this.userEntityToUserDetails = userEntityToUserDetails;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(SecurityProperties.HEADER);
        if(header == null || !header.startsWith(SecurityProperties.TOKEN_PREFIX)){
            chain.doFilter(request, response);
        }

        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityProperties.HEADER);

        if(token!= null){
            //verify token and get username from token
            String username = JWT.require(Algorithm.HMAC512(SecurityProperties.SECRET.getBytes()))
                    .build()
                    .verify(token.replace(SecurityProperties.TOKEN_PREFIX, ""))
                    .getSubject();

            if(username!=null){
                UserEntity byUserEntityName = userRepository.findByUsername(username);
                UserDetails userDetails = userEntityToUserDetails.convert(byUserEntityName);
                if(userDetails!= null) {
                    return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                }
            }
        }
        return null;
    }
}
