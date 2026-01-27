package org.example.projecthubbackend;

import jakarta.transaction.Transactional;
import lombok.Data;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
@Transactional
public class DataLoader implements ApplicationRunner {
    private UserRepository userRepository;

    @Autowired
    DataLoader(UserRepository userService) {
        this.userRepository = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<String> roles = new ArrayList<>();
        roles.add("admin");

        User user = User.builder().
                firstName("alexis").
                lastName("dupont").
                email("test@test.com").
                password(new BCryptPasswordEncoder().encode("Ppassword1$")).
                roles(roles).build();
        if (!userRepository.existsUserByEmail(user.getEmail())) {
            userRepository.save(user);
        }
    }
}

