package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.exceptions.CustomerRegistrationException;
import com.starlightonlinestore.data.models.*;
import com.starlightonlinestore.data.repository.OtpTokenRepository;
import com.starlightonlinestore.data.repository.ProductRepository;
import com.starlightonlinestore.data.repository.UserRepository;
import com.starlightonlinestore.data.dto.Response.CreateVendorResponse;
import com.starlightonlinestore.data.dto.Response.LoginResponse;
import com.starlightonlinestore.data.dto.Response.StoreResponse;
import com.starlightonlinestore.utils.validators.EmailService;
import com.starlightonlinestore.utils.validators.Token;
import com.starlightonlinestore.utils.validators.UserDetailsValidator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.starlightonlinestore.data.models.Role.VENDOR;
import static com.starlightonlinestore.data.models.Status.UNVERIFIED;
import static com.starlightonlinestore.data.models.Status.VERIFIED;

@Service
public class VendorServiceImpl implements VendorService {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final OtpTokenRepository otpTokenRepository;

    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public VendorServiceImpl(UserRepository userRepository, ProductRepository productRepository, OtpTokenRepository otpTokenRepository, EmailService emailService, UserService userService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.otpTokenRepository = otpTokenRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Override
    public String register(CreateVendorRequest createVendorRequest) {
        if(!UserDetailsValidator.isValidEmailAddress(createVendorRequest.getEmailAddress()))
            throw new CustomerRegistrationException(String.
                    format("email %s is invalid", createVendorRequest.getEmailAddress()));
        User vendor = registeringVendor(createVendorRequest);
        userRepository.save(vendor);
        SendOtpRequest OTPRequest = new SendOtpRequest();
        OTPRequest.setEmail(createVendorRequest.getEmailAddress());
        return sendOTP(OTPRequest);

    }

    private CreateVendorResponse registeredVendorResponse(User savedVendor) {
        CreateVendorResponse createVendorResponse = new CreateVendorResponse();
        createVendorResponse.setId(savedVendor.getId());
        createVendorResponse.setStatusCode(201);
        createVendorResponse.setMessage("Successfully registered");
        return createVendorResponse;
    }

    private User registeringVendor(CreateVendorRequest createVendorRequest) {
        User vendor = new User();
        if(userRepository.findByEmail(createVendorRequest.getEmailAddress()).isPresent())
            throw new RuntimeException("This email has been taken, kindly register with another email address");
        else
            vendor.setEmail(createVendorRequest.getEmailAddress());
        vendor.setPassword(createVendorRequest.getPassword());
        vendor.setStoreName(createVendorRequest.getStoreName());
        vendor.setStatus(UNVERIFIED);
        vendor.setRole(VENDOR);
        return vendor;
    }

    @Override
    public String sendOTP(SendOtpRequest sendOtpRequest) {
        User savedVendor = userRepository.findByEmail(sendOtpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));
        return generateOtpToken(sendOtpRequest, savedVendor);
    }

    private String generateOtpToken(SendOtpRequest sendOtpRequest, User savedVendor) {
        String token = Token.generateToken(4);
        OTPToken otpToken = new OTPToken();
        otpToken.setToken(token);
        otpToken.setUser(savedVendor);
        otpToken.setCreatedAt(LocalDateTime.now());
        otpToken.setExpiredAt(LocalDateTime.now().plusMinutes(10));
        otpTokenRepository.save(otpToken);
        emailService.send(sendOtpRequest.getEmail(), emailService.buildEmail(savedVendor.getStoreName(), token));
        return "Token successfully sent to your email. Please check.";
    }


    @Override
    public CreateVendorResponse createAccount(String email, VerifyOtpRequest verifyOtpRequest) {
        verifyOTP(verifyOtpRequest);
        var savedVendor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendor does not exists"));
        userRepository.enableUser(VERIFIED, savedVendor.getEmail());
        return registeredVendorResponse(savedVendor);
    }

    @Override
    public void verifyOTP(VerifyOtpRequest verifyOtpRequest) {
        OTPToken foundToken = otpTokenRepository.findByToken(verifyOtpRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Token doesn't exist"));
        if(foundToken.getVerifiedAt() != null)
            throw new RuntimeException("Token has been used");
        if(!Objects.equals(verifyOtpRequest.getToken(), foundToken.getToken()))
            throw new RuntimeException("Incorrect token");
        if(foundToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token has expired");
        otpTokenRepository.setVerifiedAt(LocalDateTime.now(), verifyOtpRequest.getToken());
    }

//    @Override
//    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
//        Vendor forgotVendor = vendorRepository.findByEmail(forgotPasswordRequest.getEmail())
//                .orElseThrow(() -> new RuntimeException("This email is not registered"));
//        var genToken = Token.generateToken(4);
//        OTPToken otpToken = new OTPToken();
//        otpToken.setToken(genToken);
//        otpToken.setVendor(forgotVendor);
//        otpToken.setCreatedAt(LocalDateTime.now());
//        otpToken.setExpiredAt(LocalDateTime.now().plusMinutes(10));
//        otpTokenRepository.save(otpToken);
//        emailService.sendEmail(forgotPasswordRequest.getEmail(), forgotVendor.getStoreName(), genToken);
//        return "Token successfully sent to your email. Please check.";
//    }

//    @Override
//    public StoreResponse resetPassword(String email, ResetPasswordRequest resetPasswordRequest) {
//        OtpVerificationForResetPassword(resetPasswordRequest);
//        User lostVendor = userRepository.findByEmail(email).get();
//        lostVendor.setPassword(resetPasswordRequest.getPassword());
//        if(BCrypt.checkpw(resetPasswordRequest.getConfirmPassword(), resetPasswordRequest.getPassword())) {
//            vendorRepository.save(lostVendor);
//            return new StoreResponse("Your password has been reset successfully");
//        } else {
//            throw new IllegalStateException("Password does not match");
//        }
//    }

//    private void OtpVerificationForResetPassword(ResetPasswordRequest resetPasswordRequest) {
//        var foundToken = otpTokenRepository.findByToken(resetPasswordRequest.getToken())
//                .orElseThrow(() -> new RuntimeException("Token doesn't exist"));
//        if(foundToken.getVerifiedAt() != null)
//            throw new RuntimeException("Token has been used");
//        if(!Objects.equals(resetPasswordRequest.getToken(), foundToken.getToken()))
//            throw new RuntimeException("Incorrect token");
//        if(foundToken.getExpiredAt().isBefore(LocalDateTime.now()))
//            throw new RuntimeException("Token has expired");
//        otpTokenRepository.setVerifiedAt(LocalDateTime.now(), resetPasswordRequest.getToken());
//    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @Override
    public StoreResponse changePassword(String email, ChangePasswordRequest changePasswordRequest) {
        return userService.changePassword(email, changePasswordRequest);
    }

    @Override
    public StoreResponse updateVendor(Integer id, UpdateVendorRequest updateVendorRequest) {
        var findVendor = userRepository.findById(id);
                if(findVendor.isEmpty()) throw new RuntimeException("Vendor not found");
        User foundVendor = updatingVendor(updateVendorRequest, findVendor);
        updatingVendor2(updateVendorRequest, foundVendor);
        userRepository.save(foundVendor);
        return new StoreResponse("Vendor has been updated");
    }

    private User updatingVendor(UpdateVendorRequest updateVendorRequest, Optional<User> findVendor) {
        var foundVendor = findVendor.get();
        if(userRepository.findByPhoneNumber(updateVendorRequest.getPhone()).isPresent())
            throw new RuntimeException("This Phone Number has been taken, kindly provide with another");
        else
            foundVendor.setPhoneNumber(updateVendorRequest.getPhone()!= null && !updateVendorRequest.getPhone()
                    .equals("") ? updateVendorRequest.getPhone() : foundVendor.getPhoneNumber());
        foundVendor.setStoreName(updateVendorRequest.getStoreName() != null && !updateVendorRequest.getStoreName()
                .equals("") ? updateVendorRequest.getStoreName() : foundVendor.getStoreName());
        return foundVendor;
    }


    private void updatingVendor2(UpdateVendorRequest updateVendorRequest, User foundVendor) {
        foundVendor.setEmail(updateVendorRequest.getEmail() != null && !updateVendorRequest.getEmail()
                .equals("") ? updateVendorRequest.getEmail() : foundVendor.getEmail());
        Set<String> vendorStoreAddress = foundVendor.getStoreAddress();
        vendorStoreAddress.add(updateVendorRequest.getStoreAddress() != null && !updateVendorRequest.getStoreAddress()
                .equals("") ? updateVendorRequest.getStoreAddress() : String.valueOf(vendorStoreAddress));
    }

    @Override
    public StoreResponse deleteVendor(Integer id, DeleteRequest deleteRequest) {
        User foundVendor = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Vendor doesn't exist"));
//        String deleteToken = UUID.randomUUID().toString();
        if(BCrypt.checkpw(deleteRequest.getPassword(), foundVendor.getPassword())) {
            String deleteVendor = "Deleted" + " + " + foundVendor.getEmail();
            foundVendor.setEmail(deleteVendor);
            userRepository.save(foundVendor);
            return new StoreResponse("Vendor deleted");
        } else {
            throw new RuntimeException("Vendor can't be deleted");
        }
    }

    @Override
    public StoreResponse addProduct(AddProductRequest addProductRequest) {
//        User savedVendor = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        Product product = new Product();
        product.setProductName(addProductRequest.getProductName());
        product.setQuantity(addProductRequest.getProductQuantity());
        product.setUnitPrice(addProductRequest.getPrice());
        product.setCategory(ProductCategory.valueOf(addProductRequest.getCategory().toUpperCase()));
//        savedVendor.getProductList().add(product);
        productRepository.save(product);
//        productService.createProduct(addProductRequest);

        return new StoreResponse("Product has been added successfully");
    }
}
