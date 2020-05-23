package com.aga.security.app.service;


import com.aga.security.app.model.UserEntity;
import com.aga.security.app.model.UserEntityToUserDetails;
import com.aga.security.app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserEntityToUserDetails userEntityToUserDetails;

    public UserPrincipalDetailsService(UserRepository userRepository, UserEntityToUserDetails userEntityToUserDetails) {
        this.userRepository = userRepository;
        this.userEntityToUserDetails = userEntityToUserDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity byUserEntityName = userRepository.findByUsername(userName);

        return userEntityToUserDetails.convert(byUserEntityName);
    }
}
