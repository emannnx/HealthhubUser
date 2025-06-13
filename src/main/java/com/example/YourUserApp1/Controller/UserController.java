package com.example.YourUserApp1.Controller;

import com.example.YourUserApp1.Model.User;
import com.example.YourUserApp1.Repository.UserRepository;
import com.example.YourUserApp1.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(
        origins = {
                "https://healthcaresyst.netlify.app",
                "http://localhost:5175"
        },
        allowCredentials = "true"
)
@RestController
@RequestMapping("/home")
public class UserController {

    record Task(int id, String task, String status){}

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/get/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/createuser")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            return ResponseEntity.badRequest().body("Username is required.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        User savedUser = userService.createUser(user); // Save user via service
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }

        User user = userOpt.get();
        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());

        if (!passwordMatch) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteall")
    public String deleteAllUsers() {
        userService.deleteAllUsers();
        return "All users deleted successfully!";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUserById(@PathVariable String id) {
        userService.deleteUserById(id);
        return "User with ID " + id + " deleted successfully!";
    }

    @DeleteMapping("/delete/username/{username}")
    public String deleteUserByUsername(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return "User with username '" + username + "' deleted successfully!";
    }

    @DeleteMapping("/delete/email/{email}")
    public String deleteUserByEmail(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return "User with email " + email + " deleted successfully!";
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody User updatedUser) {
        try {
            User savedUser = userService.updateUserByUsername(username, updatedUser);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
        }
    }
}