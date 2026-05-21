package com.cbe.banking.config;

import com.cbe.banking.model.Permission;
import com.cbe.banking.model.User;
import com.cbe.banking.repository.PermissionRepository;
import com.cbe.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Seed Permissions
            Permission readPerm = createPermissionIfNotFound("READ_PRIVILEGE");
            Permission writePerm = createPermissionIfNotFound("WRITE_PRIVILEGE");
            Permission deletePerm = createPermissionIfNotFound("DELETE_PRIVILEGE");

            // Seed Admin User
            User admin = User.builder()
                    .fullName("Branch Admin")
                    .phoneNumber("0911000000")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .permissions(Set.of(readPerm, writePerm, deletePerm))
                    .build();
            userRepository.save(admin);

            // Seed Customer User
            User customer = User.builder()
                    .fullName("Abebe Bikila")
                    .phoneNumber("0922000000")
                    .password(passwordEncoder.encode("user123"))
                    .role(User.Role.CUSTOMER)
                    .permissions(Set.of(readPerm))
                    .build();
            userRepository.save(customer);
        }
    }

    private Permission createPermissionIfNotFound(String name) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(Permission.builder().name(name).build()));
    }
}
