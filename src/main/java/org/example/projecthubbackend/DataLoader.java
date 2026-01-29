package org.example.projecthubbackend;

import jakarta.transaction.Transactional;
import lombok.Data;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Data
@Transactional
public class DataLoader implements ApplicationRunner {
    private UserRepository userRepository;

    @Value("${EMAIL}")
    private  String email;

    @Value("${PASSWORD}")
    private  String password;

    @Autowired
    DataLoader(UserRepository userService) {
        this.userRepository = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        User user = User.builder().
                firstName("alexis").
                lastName("dupont").
                email(email).
                password(new BCryptPasswordEncoder().encode(password)).
                roles(roles).build();
        if (!userRepository.existsUserByEmail(user.getEmail())) {
            userRepository.save(user);
        }
    }
}

