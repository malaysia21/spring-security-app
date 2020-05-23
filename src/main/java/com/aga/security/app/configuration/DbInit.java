package com.aga.security.app.configuration;

import com.aga.security.app.model.PermissionEntity;
import com.aga.security.app.model.RoleEntity;
import com.aga.security.app.model.UserEntity;
import com.aga.security.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Service
public class DbInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        userRepository.deleteAll();

        UserEntity agnes = new UserEntity("agnes1313", passwordEncoder.encode("pass"), Collections.singleton(new RoleEntity("USER")), null);
        UserEntity admin = new UserEntity("admin", passwordEncoder.encode("pass"), Collections.singleton(new RoleEntity("ADMIN")), new HashSet<>(Arrays.asList(new PermissionEntity("ACCESS_ADVERTISEMENT"), new PermissionEntity("ACCESS_ADVERTISEMENTS"))));
        UserEntity manager = new UserEntity("manager", passwordEncoder.encode("pass"), Collections.singleton(new RoleEntity("MANAGER")), Collections.singleton(new PermissionEntity("ACCESS_ADVERTISEMENT")));

        userRepository.saveAll(Arrays.asList(agnes, admin, manager));

    }
}
