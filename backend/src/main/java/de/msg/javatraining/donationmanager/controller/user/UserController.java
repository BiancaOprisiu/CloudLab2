package de.msg.javatraining.donationmanager.controller.user;

import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.service.role.RoleServiceException;
import de.msg.javatraining.donationmanager.service.user.UserService;
import de.msg.javatraining.donationmanager.service.user.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> findAll() throws UserServiceException {
        try {
            List<User> users = userService.findAll();
            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(users);
            } else {
                return ResponseEntity.ok(users);
            }
        } catch (UserServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity <?> findById(@PathVariable Long id){
        try {
            User user=userService.findById(id);
            return ResponseEntity.ok(user);
        }
        catch (UserServiceException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT')")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        try{
            userService.deleteById(id);
            return ResponseEntity.ok("OK");
        } catch (UserServiceException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT')")
        public ResponseEntity<?> createUser(@RequestBody User user){
        try{
            User createdUser=userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }catch (UserServiceException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT')")
    public ResponseEntity <?> updateUserById(@PathVariable Long id, @RequestBody User updatedUser){
        try{
            User updated=userService.updateUserById(id,updatedUser);
            return ResponseEntity.ok(updated);
        }
        catch (UserServiceException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RoleServiceException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/users/{id}/activate")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT')")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        try {
            User activatedUser = userService.activateUser(id);
            return ResponseEntity.ok(activatedUser);
        } catch (UserServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        try {
            User deactivatedUser = userService.deactivateUser(id);
            return ResponseEntity.ok(deactivatedUser);
        } catch (UserServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/search")
    public ResponseEntity<?> searchUserByUsername(@RequestParam String username) {
        Optional<User> optionalUser = userService.getUserByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
