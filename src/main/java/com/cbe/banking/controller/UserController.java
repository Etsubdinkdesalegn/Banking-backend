package com.cbe.banking.controller;

import com.cbe.banking.model.User;
import com.cbe.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "uploads/profiles/";

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    public ResponseEntity<User> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody User updateRequest
    ) {
        user.setFullName(updateRequest.getFullName());
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/me/avatar")
    public ResponseEntity<String> uploadAvatar(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, file.getBytes());

        user.setProfileImage("/api/v1/public/avatar/" + fileName);
        userRepository.save(user);

        return ResponseEntity.ok(user.getProfileImage());
    }

    @GetMapping({"/avatar/{fileName}", "/public/avatar/{fileName}"})
    public ResponseEntity<byte[]> getAvatar(@PathVariable String fileName) throws IOException {
        Path path = Paths.get(UPLOAD_DIR + fileName);
        if (Files.exists(path)) {
            byte[] image = Files.readAllBytes(path);
            String contentType = Files.probeContentType(path);
            return ResponseEntity.ok()
                    .header("Content-Type", contentType != null ? contentType : "image/jpeg")
                    .body(image);
        }
        return ResponseEntity.notFound().build();
    }
}
