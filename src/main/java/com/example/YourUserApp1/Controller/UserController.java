//package com.example.YourUserApp1.Controller;
//
//import com.example.YourUserApp1.Model.User;
//import com.example.YourUserApp1.Repository.UserRepository;
//import com.example.YourUserApp1.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/home")
//public class UserController {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @GetMapping("/users")
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    @PostMapping("/createuser")
//    public User createUser(@RequestBody User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password before saving
//        return userRepository.save(user);
//    }
//
//    private UserService userService;
//
//    @DeleteMapping("/deleteall")
//    public String deleteAllUsers() {
//        userService.deleteAllUsers();
//        return "All users deleted successfully!";
//    }
//}


//
//
//package com.example.YourUserApp1.Controller;
//
//import com.example.YourUserApp1.Model.User;
//import com.example.YourUserApp1.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/home")
//public class UserController {
//    private final UserService userService;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
//        this.userService = userService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @GetMapping("/users")
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @PostMapping("/createuser")
//    public User createUser(@RequestBody User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password before saving
//        return userService.createUser(user);
//    }
//
//    @DeleteMapping("/deleteall")
//    public String deleteAllUsers() {
//        userService.deleteAllUsers();
//        return "All users deleted successfully!";
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public String deleteUserById(@PathVariable String id) {
//        userService.deleteUserById(id);
//        return "User with ID " + id + " deleted successfully!";
//    }
//}


package com.example.YourUserApp1.Controller;

import com.example.YourUserApp1.Model.User;
import com.example.YourUserApp1.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/createuser")
    public User createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password before saving
        return userService.createUser(user);
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

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }
}
