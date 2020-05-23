package com.aga.security.app.model;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
@Component
public class UserEntityToUserDetails implements Converter<UserEntity, UserDetails> {

    @Override
    public UserDetails convert(UserEntity userEntity) {
        UserDetailsImpl userDetails = new UserDetailsImpl();

        userDetails.setUsername(userEntity.getUsername());
        userDetails.setPassword(userEntity.getPassword());
        userDetails.setEnabled(userEntity.isActive());

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userEntity.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole())));
        userEntity.getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getPermission())));

        userDetails.setAuthorities(authorities);
        return userDetails;
    }


}
