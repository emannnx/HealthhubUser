//package com.example.YourUserApp1;
//
//import com.example.YourUserApp1.Model.User;
//import com.example.YourUserApp1.Repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//public class UserService implements UserDetailsService {
//    private final UserRepository userRepository;
//    private final MongoTemplate mongoTemplate;
//
//    @Autowired
//    public UserService(UserRepository userRepository, MongoTemplate mongoTemplate) {
//        this.userRepository = userRepository;
//        this.mongoTemplate = mongoTemplate;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//
//        return org.springframework.security.core.userdetails.User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .authorities(Collections.emptyList()) // No roles assigned
//                .build();
//    }
//
//    public void deleteAllUsers() {
//        userRepository.deleteAll();
//        System.out.println("All users deleted successfully.");
//    }
//
//
//
//}

//
//package com.example.YourUserApp1;
//
//import com.example.YourUserApp1.Model.User;
//import com.example.YourUserApp1.Repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.List;
//
//@Service
//public class UserService implements UserDetailsService {
//    private final UserRepository userRepository;
//    private final MongoTemplate mongoTemplate;
//
//    @Autowired
//    public UserService(UserRepository userRepository, MongoTemplate mongoTemplate) {
//        this.userRepository = userRepository;
//        this.mongoTemplate = mongoTemplate;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//
//        return org.springframework.security.core.userdetails.User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .authorities(Collections.emptyList()) // No roles assigned
//                .build();
//    }
//
//    // Create a user
//    public User createUser(User user) {
//        return userRepository.save(user);
//    }
//
//    // Get all users
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    // Delete user by ID
//    public void deleteUserById(String id) {
//        userRepository.deleteById(id);
//    }
//
//    // Delete all users
//    public void deleteAllUsers() {
//        userRepository.deleteAll();
//        System.out.println("All users deleted successfully.");
//    }
//}


package com.example.YourUserApp1;

import com.example.YourUserApp1.Model.User;
import com.example.YourUserApp1.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, MongoTemplate mongoTemplate, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.emptyList()) // No roles assigned
                .build();
    }

    // Create a user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete user by ID
    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    // Delete all users
    public void deleteAllUsers() {
        userRepository.deleteAll();
        System.out.println("All users deleted successfully.");
    }

    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        userRepository.delete(user);
    }

    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }


    // Update user
    public User updateUser(String id, User updatedUser) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            // Update username if provided
            if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
                existingUser.setUsername(updatedUser.getUsername());
            }

            // Update password if provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            // Save updated user
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found with ID: " + id);
        }
    }

    public User updateUserByUsername(String username, User updatedUser) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with username: " + username);
        }

        User existingUser = optionalUser.get();

        // Check for conflicts
        if (!existingUser.getUsername().equals(updatedUser.getUsername())
                && userRepository.findByUsername(updatedUser.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }

        if (!existingUser.getEmail().equals(updatedUser.getEmail())
                && userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists.");
        }

        // Update values
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setHeight(updatedUser.getHeight());
        existingUser.setWeight(updatedUser.getWeight());
        existingUser.setBloodType(updatedUser.getBloodType());
        existingUser.setGenotype(updatedUser.getGenotype());
        existingUser.setOxygenLevel(updatedUser.getOxygenLevel());
        existingUser.setMedicalConditions(updatedUser.getMedicalConditions());

        existingUser.setFamilyMedicalHistory(updatedUser.isFamilyMedicalHistory());
        existingUser.setFamilyHistoryText(
                updatedUser.isFamilyMedicalHistory() ? updatedUser.getFamilyHistoryText() : null
        );

        // Optional password update
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }

}
