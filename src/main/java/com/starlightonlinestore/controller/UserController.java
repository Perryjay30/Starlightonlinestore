package com.starlightonlinestore.controller;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.models.User;
import com.starlightonlinestore.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/customer")
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CustomerRegistrationRequest
                                              customerRegistrationRequest) {
        return ResponseEntity.ok(userService.register(customerRegistrationRequest));
    }

    @PostMapping("/createAccount/{email}")
    public ResponseEntity<?> createAccount(@PathVariable String email, @Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createAccount(email, verifyOtpRequest));
    }


    @GetMapping("/login")
    //Social login links:
    //Google: http://localhost:8082/login/oauth2/code/google
    //Github: http://localhost:8082/login/oauth2/code/github
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping("/forgotPassword")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        return ResponseEntity.ok(userService.forgotPassword(forgotPasswordRequest));
    }

    @PostMapping("/resetPassword/{email}")
    @PreAuthorize("hasAnyAuthority('USER', 'VENDOR')")
    public ResponseEntity<?> resetPassword(@PathVariable String email, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok(userService.resetPassword(email, resetPasswordRequest));
    }

    @PostMapping("/changePassword/{email}")
    @PreAuthorize("hasAnyAuthority('USER', 'VENDOR')")
    public ResponseEntity<?> changePassword(@PathVariable String email, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(userService.changePassword(email, changePasswordRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> deleteCustomerById(@PathVariable int id, @Valid @RequestBody DeleteRequest deleteRequest) {
        log.info("Id -> {}", id);
      return ResponseEntity.ok(userService.deleteCustomer(id, deleteRequest));
    }

    @PatchMapping("/updateProfile/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updateCustomer(@PathVariable int id, @Valid @RequestBody EditCustomerProfileRequest editCustomerProfileRequest) {
       return ResponseEntity.ok(userService.updateCustomer(id, editCustomerProfileRequest));
    }

    @PostMapping("/assignRoles")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> assignRoles(@RequestBody AssignRoleRequest assignRoleRequest) throws MessagingException {
        return ResponseEntity.ok(userService.assignRoles(assignRoleRequest));
    }

    @GetMapping("getAllUsers")
//    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public List<User> getAllUsers() {
       return userService.getAllUsers();
    }



}
