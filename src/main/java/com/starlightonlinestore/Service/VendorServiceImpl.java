package com.starlightonlinestore.Service;

import com.starlightonlinestore.Data.Exceptions.CustomerRegistrationException;
import com.starlightonlinestore.Data.Models.Product;
import com.starlightonlinestore.Data.Models.ProductCategory;
import com.starlightonlinestore.Data.Models.Vendor;
import com.starlightonlinestore.Data.Repository.ProductRepository;
import com.starlightonlinestore.Data.Repository.VendorRepository;
import com.starlightonlinestore.Data.dto.Request.AddProductRequest;
import com.starlightonlinestore.Data.dto.Request.CreateVendorRequest;
import com.starlightonlinestore.Data.dto.Request.LoginRequest;
import com.starlightonlinestore.Data.dto.Request.UpdateRequest;
import com.starlightonlinestore.Data.dto.Response.CreateVendorResponse;
import com.starlightonlinestore.Data.dto.Response.LoginResponse;
import com.starlightonlinestore.Data.dto.Response.Response;
import com.starlightonlinestore.utils.validators.UserDetailsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class VendorServiceImpl implements VendorService {
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ProductService productService;

//    @Autowired
//    private ProductRepository productRepository;

    @Override
    public CreateVendorResponse createVendor(CreateVendorRequest createVendorRequest) {
        if(!UserDetailsValidator.isValidEmailAddress(createVendorRequest.getEmailAddress()))
            throw new CustomerRegistrationException(String.
                    format("email %s is invalid", createVendorRequest.getEmailAddress()));

        if(!UserDetailsValidator.isValidPassword(createVendorRequest.getPassword()))
            throw new CustomerRegistrationException(String.
                    format("password %s is too weak", createVendorRequest.getPassword()));

        if(!UserDetailsValidator.isValidPhoneNumber(createVendorRequest.getPhoneNumber()))
            throw new CustomerRegistrationException(String.
                    format("PhoneNumber %s is invalid", createVendorRequest.getPhoneNumber()));
        Vendor newVendor = registeringVendor(createVendorRequest);
        Vendor savedVendor = vendorRepository.save(newVendor);
        return registeredVendorResponse(savedVendor);

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
        vendor.setEmail(createVendorRequest.getEmailAddress());
        vendor.setPassword(createVendorRequest.getPassword());
        vendor.setStoreName(createVendorRequest.getStoreName());
        vendor.setPhoneNumber(createVendorRequest.getPhoneNumber());
        Set<String> vendorStoreAddress = vendor.getStoreAddress();
        vendorStoreAddress.add(createVendorRequest.getStoreAddress());
        return vendor;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Vendor loggingIn = vendorRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Incorrect Email"));

        LoginResponse res = new LoginResponse();
        if (loggingIn.getPassword().equals(loginRequest.getPassword())) {
            res.setMessage("login is successful");
        return res;
        }
        res.setMessage("failed to login");
        return res;
    }

    @Override
    public Response updateVendor(UpdateRequest updateRequest) {
        var findVendor = vendorRepository.findById(updateRequest.getId());
                if(findVendor.isEmpty()) throw new RuntimeException("Vendor not found");
        Vendor foundVendor = updatingVendor(updateRequest, findVendor);
        updatingVendor2(updateRequest, foundVendor);
        vendorRepository.save(foundVendor);
        return new Response("Vendor has been updated");
    }

    private Vendor updatingVendor(UpdateRequest updateRequest, Optional<Vendor> findVendor) {
        var foundVendor = findVendor.get();
        foundVendor.setPhoneNumber(updateRequest.getPhone()!= null && !updateRequest.getPhone()
                .equals("") ? updateRequest.getPhone() : foundVendor.getPhoneNumber());
        foundVendor.setStoreName(updateRequest.getStoreName() != null && !updateRequest.getStoreName()
                .equals("") ? updateRequest.getStoreName() : foundVendor.getStoreName());
        foundVendor.setPassword(updateRequest.getPassword() != null && !updateRequest.getPassword()
                .equals("") ? updateRequest.getPassword() : foundVendor.getPassword());
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
    public Response deleteVendor(int id) {
        vendorRepository.deleteById(id);
        return new Response("Deleted");
    }

    @Override
    public Response addProduct(int id, AddProductRequest addProductRequest) {
        Vendor savedVendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        Product product = new Product();
        product.setName(addProductRequest.getName());
        product.setQuantity(addProductRequest.getProductQuantity());
        product.setPrice(addProductRequest.getPrice());
        product.setCategory(ProductCategory.valueOf(String.valueOf(addProductRequest
                .getCategory())));
        savedVendor.getProductList().add(product);
//        productRepository.save(product);
        productService.createProduct(addProductRequest);

        return new Response("Product has been added successfully");
    }
}
