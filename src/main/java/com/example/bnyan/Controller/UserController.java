package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.DTO.CustomerDTO;
import com.example.bnyan.Model.User;
import com.example.bnyan.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.status(200).body(userService.get());
    }


    @PutMapping("/update")
    public ResponseEntity<?> update(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid User updatedUser)
    {
        userService.update(user.getId(), updatedUser);
        return ResponseEntity.ok(new ApiResponse("User updated"));
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> delete(@PathVariable Integer userId) {
        userService.delete(userId);
        return ResponseEntity.status(200).body(new ApiResponse("User deleted"));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(userService.getUserById(id));
    }

    @GetMapping("/get-by-username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.status(200).body(userService.getUserByUsername(username));
    }

    @GetMapping("/get-by-role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.status(200).body(userService.getUsersByRole(role));
    }
}