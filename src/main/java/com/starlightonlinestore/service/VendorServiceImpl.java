package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.exceptions.CustomerRegistrationException;
import com.starlightonlinestore.data.models.*;
import com.starlightonlinestore.data.repository.OtpTokenRepository;
import com.starlightonlinestore.data.repository.ProductRepository;
import com.starlightonlinestore.data.repository.VendorRepository;
import com.starlightonlinestore.data.dto.Response.CreateVendorResponse;
import com.starlightonlinestore.data.dto.Response.LoginResponse;
import com.starlightonlinestore.data.dto.Response.Response;
import com.starlightonlinestore.utils.validators.EmailService;
import com.starlightonlinestore.utils.validators.Token;
import com.starlightonlinestore.utils.validators.UserDetailsValidator;
import jakarta.mail.MessagingException;
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

    private final VendorRepository vendorRepository;

    private final ProductRepository productRepository;

    private final OtpTokenRepository otpTokenRepository;

    private final EmailService emailService;

    @Autowired
    public VendorServiceImpl(VendorRepository vendorRepository, ProductRepository productRepository, OtpTokenRepository otpTokenRepository, EmailService emailService) {
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
        this.otpTokenRepository = otpTokenRepository;
        this.emailService = emailService;
    }

    @Override
    public String register(CreateVendorRequest createVendorRequest) {
        if(!UserDetailsValidator.isValidEmailAddress(createVendorRequest.getEmailAddress()))
            throw new CustomerRegistrationException(String.
                    format("email %s is invalid", createVendorRequest.getEmailAddress()));
        Vendor vendor = registeringVendor(createVendorRequest);
        vendorRepository.save(vendor);
        SendOtpRequest OTPRequest = new SendOtpRequest();
        OTPRequest.setEmail(createVendorRequest.getEmailAddress());
        return sendOTP(OTPRequest);

    }

    private CreateVendorResponse registeredVendorResponse(Vendor savedVendor) {
        CreateVendorResponse createVendorResponse = new CreateVendorResponse();
        createVendorResponse.setId(savedVendor.getId());
        createVendorResponse.setStatusCode(201);
        createVendorResponse.setMessage("Successfully registered");
        return createVendorResponse;
    }

    private Vendor registeringVendor(CreateVendorRequest createVendorRequest) {
        Vendor vendor = new Vendor();
        if(vendorRepository.findByEmail(createVendorRequest.getEmailAddress()).isPresent())
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
        Vendor savedVendor = vendorRepository.findByEmail(sendOtpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));
        return generateOtpToken(sendOtpRequest, savedVendor);
    }

    private String generateOtpToken(SendOtpRequest sendOtpRequest, Vendor savedVendor) {
        String token = Token.generateToken(4);
        OTPToken otpToken = new OTPToken();
        otpToken.setToken(token);
        otpToken.setVendor(savedVendor);
        otpToken.setCreatedAt(LocalDateTime.now());
        otpToken.setExpiredAt(LocalDateTime.now().plusMinutes(10));
        otpTokenRepository.save(otpToken);
        emailService.send(sendOtpRequest.getEmail(), emailService.buildEmail(savedVendor.getStoreName(), token));
        return "Token successfully sent to your email. Please check.";
    }


    @Override
    public CreateVendorResponse createAccount(VerifyOtpRequest verifyOtpRequest) {
        verifyOTP(verifyOtpRequest);
        var savedVendor = vendorRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Vendor does not exists"));
        vendorRepository.enableVendor(VERIFIED, savedVendor.getEmail());
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

    @Override
    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        Vendor forgotVendor = vendorRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("This email is not registered"));
        var genToken = Token.generateToken(4);
        OTPToken otpToken = new OTPToken();
        otpToken.setToken(genToken);
        otpToken.setVendor(forgotVendor);
        otpToken.setCreatedAt(LocalDateTime.now());
        otpToken.setExpiredAt(LocalDateTime.now().plusMinutes(10));
        otpTokenRepository.save(otpToken);
        emailService.sendEmail(forgotPasswordRequest.getEmail(), forgotVendor.getStoreName(), genToken);
        return "Token successfully sent to your email. Please check.";
    }

    @Override
    public Response resetPassword(ResetPasswordRequest resetPasswordRequest) {
        OtpVerificationForResetPassword(resetPasswordRequest);
        Vendor lostVendor = vendorRepository.findByEmail(resetPasswordRequest.getEmail()).get();
        lostVendor.setPassword(resetPasswordRequest.getPassword());
        if(BCrypt.checkpw(resetPasswordRequest.getConfirmPassword(), resetPasswordRequest.getPassword())) {
            vendorRepository.save(lostVendor);
            return new Response("Your password has been reset successfully");
        } else {
            throw new IllegalStateException("Password does not match");
        }
    }

    private void OtpVerificationForResetPassword(ResetPasswordRequest resetPasswordRequest) {
        var foundToken = otpTokenRepository.findByToken(resetPasswordRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Token doesn't exist"));
        if(foundToken.getVerifiedAt() != null)
            throw new RuntimeException("Token has been used");
        if(!Objects.equals(resetPasswordRequest.getToken(), foundToken.getToken()))
            throw new RuntimeException("Incorrect token");
        if(foundToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token has expired");
        otpTokenRepository.setVerifiedAt(LocalDateTime.now(), resetPasswordRequest.getToken());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Vendor loggingIn = vendorRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Incorrect Email"));
        if(loggingIn.getStatus() != VERIFIED) throw new RuntimeException("Vendor is not verified");
        LoginResponse res = new LoginResponse();
        if(BCrypt.checkpw(loginRequest.getPassword(), loggingIn.getPassword())) {
            res.setMessage("login is successful");
            return res;
        }
        res.setMessage("failed to login");
        return res;
    }

    @Override
    public Response changePassword(ChangePasswordRequest changePasswordRequest) {
        Vendor verifiedVendor = vendorRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Vendor isn't registered"));
        if(BCrypt.checkpw(changePasswordRequest.getOldPassword(), verifiedVendor.getPassword()))
            verifiedVendor.setPassword(changePasswordRequest.getNewPassword());
        vendorRepository.save(verifiedVendor);
        return new Response("Your password has been successfully changed");
    }

    @Override
    public Response updateVendor(Integer id, UpdateRequest updateRequest) {
        var findVendor = vendorRepository.findById(id);
                if(findVendor.isEmpty()) throw new RuntimeException("Vendor not found");
        Vendor foundVendor = updatingVendor(updateRequest, findVendor);
        updatingVendor2(updateRequest, foundVendor);
        vendorRepository.save(foundVendor);
        return new Response("Vendor has been updated");
    }

    private Vendor updatingVendor(UpdateRequest updateRequest, Optional<Vendor> findVendor) {
        var foundVendor = findVendor.get();
        if(vendorRepository.findByPhoneNumber(updateRequest.getPhone()).isPresent())
            throw new RuntimeException("This Phone Number has been taken, kindly provide with another");
        else
            foundVendor.setPhoneNumber(updateRequest.getPhone()!= null && !updateRequest.getPhone()
                    .equals("") ? updateRequest.getPhone() : foundVendor.getPhoneNumber());
        foundVendor.setStoreName(updateRequest.getStoreName() != null && !updateRequest.getStoreName()
                .equals("") ? updateRequest.getStoreName() : foundVendor.getStoreName());
        return foundVendor;
    }


    private void updatingVendor2(UpdateRequest updateRequest, Vendor foundVendor) {
        foundVendor.setEmail(updateRequest.getEmail() != null && !updateRequest.getEmail()
                .equals("") ? updateRequest.getEmail() : foundVendor.getEmail());
        Set<String> vendorStoreAddress = foundVendor.getStoreAddress();
        vendorStoreAddress.add(updateRequest.getStoreAddress() != null && !updateRequest.getStoreAddress()
                .equals("") ? updateRequest.getStoreAddress() : String.valueOf(vendorStoreAddress));
    }

    @Override
    public Response deleteVendor(Integer id, DeleteRequest deleteRequest) {
        Vendor foundVendor = vendorRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Vendor doesn't exist"));
//        String deleteToken = UUID.randomUUID().toString();
        if(BCrypt.checkpw(deleteRequest.getPassword(), foundVendor.getPassword())) {
            String deleteVendor = "Deleted" + " + " + foundVendor.getEmail();
            foundVendor.setEmail(deleteVendor);
            vendorRepository.save(foundVendor);
            return new Response("Vendor deleted");
        } else {
            throw new RuntimeException("Vendor can't be deleted");
        }
    }

    @Override
    public Response addProduct(int id, AddProductRequest addProductRequest) {
        Vendor savedVendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        Product product = new Product();
        product.setName(addProductRequest.getName());
        product.setQuantity(addProductRequest.getProductQuantity());
        product.setPrice(addProductRequest.getPrice());
        product.setCategory(ProductCategory.GROCERIES);
        savedVendor.getProductList().add(product);
        productRepository.save(product);
//        productService.createProduct(addProductRequest);

        return new Response("Product has been added successfully");
    }
}
