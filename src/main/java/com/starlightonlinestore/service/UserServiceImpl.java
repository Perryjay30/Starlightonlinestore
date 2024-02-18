package com.starlightonlinestore.service;

import com.starlightonlinestore.config.JwtService;
import com.starlightonlinestore.data.exceptions.CustomerRegistrationException;
import com.starlightonlinestore.data.exceptions.StoreException;
import com.starlightonlinestore.data.models.*;
import com.starlightonlinestore.data.repository.UserRepository;
import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.dto.Response.*;
import com.starlightonlinestore.data.repository.OtpTokenRepository;
import com.starlightonlinestore.utils.validators.EmailService;
import com.starlightonlinestore.utils.validators.Token;
import com.starlightonlinestore.utils.validators.UserDetailsValidator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.starlightonlinestore.data.models.Role.*;
import static com.starlightonlinestore.data.models.Status.UNVERIFIED;
import static com.starlightonlinestore.data.models.Status.VERIFIED;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final OtpTokenRepository otpTokenRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(CustomerRegistrationRequest registrationRequest) {
        if(!UserDetailsValidator.isValidEmailAddress(registrationRequest.getEmail()))
            throw new CustomerRegistrationException(String.
                    format("email %s is invalid", registrationRequest.getEmail()));
        User user = OnboardUser(registrationRequest);
        userRepository.save(user);
        SendOtpRequest OTPRequest = new SendOtpRequest();
        OTPRequest.setEmail(registrationRequest.getEmail());
        return sendOTP(OTPRequest);
    }

    @Override
    public CustomerRegistrationResponse createAccount(String email, VerifyOtpRequest verifyOtpRequest) {
//        log.info(verifyOtpRequest.getEmail());
//        log.info(verifyOtpRequest.getToken());
        verifyOTP(verifyOtpRequest);
        var savedCustomer =
                getFoundCustomer(userRepository.findByEmail(email), "Customer does not exists");
//        savedCustomer.setStatus(VERIFIED);
//        customerRepository.save(savedCustomer);
        userRepository.enableUser(VERIFIED, savedCustomer.getEmail());
        return buildBuyerRegistrationResponse(savedCustomer);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User foundUser = getFoundCustomer(userRepository.findByEmail(loginRequest
                .getEmail()), "your email is incorrect");
        if(foundUser.getStatus() != VERIFIED) throw new RuntimeException("Customer is not verified");
        Authentication authenticating = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        String loginMessage, token;
        if(authenticating.isAuthenticated()) {
            token = jwtService.generateToken(loginRequest.getEmail());
            loginMessage = "Login Successful!!";
            return new LoginResponse(loginMessage, token);
        } else
            throw new UsernameNotFoundException("Invalid credentials!!");
    }

    @Override
    public void verifyOTP(VerifyOtpRequest verifyOtpRequest) {
        OTPToken foundToken = otpTokenRepository.findByToken(verifyOtpRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Token doesn't exist"));
        if(foundToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token has expired");
        if(foundToken.getVerifiedAt() != null)
            throw new RuntimeException("Token has been used");
        if(!Objects.equals(verifyOtpRequest.getToken(), foundToken.getToken()))
            throw new RuntimeException("Incorrect token");
        otpTokenRepository.setVerifiedAt(LocalDateTime.now(), verifyOtpRequest.getToken());
//        var token = otpTokenRepository.findByToken(foundToken.getToken()).orElseThrow(()->new RuntimeException("token not found"));
//        token.setVerifiedAt(LocalDateTime.now());
//        otpTokenRepository.save(token);
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        User forgotUser = getFoundCustomer(userRepository.findByEmail(forgotPasswordRequest.getEmail()), "This email is not registered");
        String token = Token.generateToken(4);
        OTPToken otpToken = new OTPToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), forgotUser);
        otpTokenRepository.save(otpToken);
        emailService.sendEmail(forgotPasswordRequest.getEmail(), forgotUser.getFirstName(), token);
        return "Token successfully sent to your email. Please check.";
//        SendOtpRequest request = new SendOtpRequest();
//        request.setEmail(forgotPasswordRequest.getEmail());
//        return generateOtpToken(request, forgotCustomer);
    }

    @Override
    public StoreResponse resetPassword(String email, ResetPasswordRequest resetPasswordRequest) {
        VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest();
        verifyOtpRequest.setToken(resetPasswordRequest.getToken());
        verifyOTP(verifyOtpRequest);
        User foundUser = userRepository.findByEmail(email).get();
        foundUser.setPassword(resetPasswordRequest.getPassword());
        if(BCrypt.checkpw(resetPasswordRequest.getConfirmPassword(), resetPasswordRequest.getPassword())) {
            userRepository.save(foundUser);
            return new StoreResponse("Your password has been reset successfully");
        } else {
            throw new IllegalStateException("Password does not match");
        }
    }

//    private void verifyOtpForResetPassword(ResetPasswordRequest resetPasswordRequest) {
//        VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest();
//        verifyOTP(verifyOtpRequest);
//    }

    @Override
    public String sendOTP(SendOtpRequest sendOtpRequest) {
        User savedUser = getFoundCustomer(userRepository.findByEmail(sendOtpRequest.getEmail()), "Email not found");
        return generateOtpToken(sendOtpRequest, savedUser);
    }

    private String generateOtpToken(SendOtpRequest sendOtpRequest, User savedUser) {
        String token = Token.generateToken(4);
        OTPToken otpToken = new OTPToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), savedUser);
        otpTokenRepository.save(otpToken);
        emailService.send(sendOtpRequest.getEmail(), emailService.buildEmail(savedUser.getFirstName(), token));
        return "Token successfully sent to your email. Please check.";

    }
    private CustomerRegistrationResponse buildBuyerRegistrationResponse(User savedUser) {
        CustomerRegistrationResponse response = new CustomerRegistrationResponse();
        response.setMessage("User registration successful");
        response.setStatusCode(201);
        response.setUserId(savedUser.getId());
        return response;
    }

    private User OnboardUser (CustomerRegistrationRequest registrationRequest) {
        User user = new User();
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        if(userRepository.findByEmail(registrationRequest.getEmail()).isPresent())
            throw new RuntimeException("This email has been taken, kindly register with another email address");
        else
            user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());
        user.setStatus(UNVERIFIED);
        user.setRole(USER);
        user.setAuthProvider(AuthProvider.LOCAL);
        return user;
    }

    @Override
    public StoreResponse changePassword(String email, ChangePasswordRequest changePasswordRequest) {
        User verifiedUser = getFoundCustomer(userRepository.findByEmail(email), "customer isn't registered");
        if(BCrypt.checkpw(changePasswordRequest.getOldPassword(), verifiedUser.getPassword()))
            verifiedUser.setPassword(changePasswordRequest.getNewPassword());
        userRepository.save(verifiedUser);
        return new StoreResponse("Your password has been successfully changed");
    }

    @Override
    public StoreResponse deleteCustomer(int id, DeleteRequest deleteRequest) {
        User foundUser = getFoundCustomer(userRepository.findById(id), "Customer doesn't exist");
        String randomToken = UUID.randomUUID().toString();
        String encoded = BCrypt.hashpw(randomToken, BCrypt.gensalt());
        if (BCrypt.checkpw(deleteRequest.getPassword(), foundUser.getPassword())) {
            String deleteCustomer = "Deleted" + " " + foundUser.getEmail() + " " + encoded;
            foundUser.setEmail(deleteCustomer);
            userRepository.save(foundUser);
            return new StoreResponse("Customer deleted");
        } else {
            throw new RuntimeException("Customer can't be deleted");
        }
    }

    @Override
    public User getFoundCustomer(Optional<User> userRepository, String message) {
        return userRepository.orElseThrow(
                () -> new RuntimeException(message));
    }

    @Override
    public StoreResponse updateCustomer(Integer id, EditCustomerProfileRequest editCustomerProfileRequest) {
        var customer = userRepository.findById(id);
        if (customer.isEmpty()) throw new RuntimeException("customer not found");
        User foundUser = updatingTheCustomer(editCustomerProfileRequest, customer);
        userRepository.save(foundUser);
        return new StoreResponse("Customer updated successfully");
    }

    @Override
    public StoreResponse assignRoles(AssignRoleRequest assignRoleRequest) throws MessagingException {
        User existingUser = userRepository.findByEmail(assignRoleRequest.getEmail())
                .orElseThrow(() -> new StoreException("User isn't available"));
//        Role role = new Role();
//        role.setRoleName(assignRoleRequest.getUserRole());
        existingUser.setRole(Role.valueOf(assignRoleRequest.getUserRole().toUpperCase()));
        userRepository.save(existingUser);
//        emailService.emailForAssignRole(existingUser.getEmail(), existingUser.getFirstName());
        return new StoreResponse(existingUser.getFirstName() + " is now a  " +
                " " + existingUser.getRole());
    }

    private User updatingTheCustomer(EditCustomerProfileRequest editCustomerProfileRequest, Optional<User> customer) {
        var foundCustomer = customer.get();
        foundCustomer.setFirstName(editCustomerProfileRequest.getFirstName());
        if(userRepository.findByPhoneNumber(editCustomerProfileRequest.getPhone()).isPresent())
            throw new RuntimeException("This Phone Number has been taken, kindly register with another");
        else
            foundCustomer.setPhoneNumber(editCustomerProfileRequest.getPhone()!= null && !editCustomerProfileRequest.getPhone()
                .equals("") ? editCustomerProfileRequest.getPhone() : foundCustomer.getPhoneNumber());
        return stillUpdatingCustomer(editCustomerProfileRequest, foundCustomer);
    }

    private User stillUpdatingCustomer(EditCustomerProfileRequest editCustomerProfileRequest, User foundUser) {
        foundUser.setLastName(editCustomerProfileRequest.getLastName());
        Set<String> buyersAddressList = foundUser.getDeliveryAddress();
        buyersAddressList.add(editCustomerProfileRequest.getDeliveryAddress());
        foundUser.setEmail(editCustomerProfileRequest.getEmail() != null && !editCustomerProfileRequest.getEmail()
                .equals("") ? editCustomerProfileRequest.getEmail() : foundUser.getEmail());
        return foundUser;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
