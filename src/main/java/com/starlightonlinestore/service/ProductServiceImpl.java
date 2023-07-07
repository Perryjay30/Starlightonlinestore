package com.starlightonlinestore.service;

import com.starlightonlinestore.data.exceptions.StoreException;
import com.starlightonlinestore.data.models.Product;
import com.starlightonlinestore.data.models.ProductCategory;
import com.starlightonlinestore.data.models.Vendor;
import com.starlightonlinestore.data.repository.CustomerRepository;
import com.starlightonlinestore.data.repository.ProductRepository;
import com.starlightonlinestore.data.dto.Request.AddProductRequest;
import com.starlightonlinestore.data.dto.Request.ProductUpdateRequest;
import com.starlightonlinestore.data.dto.Response.AddProductResponse;
import com.starlightonlinestore.data.dto.Response.StoreResponse;
import com.starlightonlinestore.data.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;

    private final VendorRepository vendorRepository;


    @Override
    public AddProductResponse createProduct(AddProductRequest addProductRequest) {
        Product product = new Product();
        product.setUnitPrice(addProductRequest.getPrice());
        product.setCategory(ProductCategory.valueOf(String.valueOf(addProductRequest
                .getCategory())));
        product.setProductName(addProductRequest.getProductName());
        product.setQuantity(addProductRequest.getProductQuantity());
        Product savedProduct = productRepository.save(product);

        AddProductResponse response = new AddProductResponse();
        response.setProductId(savedProduct.getId());
        response.setMessage("product added successfully");
        response.setStatusCode(201);
        return response;
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public List<Product> viewAllProduct(Integer customerId) {
        customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        return productRepository.findAll();
    }

    @Override
    public StoreResponse updateProduct(Integer userId, Integer productId, ProductUpdateRequest productUpdateRequest) {
        findVendor(userId);
        var foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new StoreException("Product can't be found"));
        updatingProduct(productUpdateRequest, foundProduct);
        productRepository.save(foundProduct);
        return new StoreResponse("Product update successful");
    }

    private void updatingProduct(ProductUpdateRequest productUpdateRequest, Product foundProduct) {
        foundProduct.setCategory(productUpdateRequest.getCategory());
        foundProduct.setProductName(productUpdateRequest.getProductName() != null && !productUpdateRequest.getProductName().equals("")
                ? productUpdateRequest.getProductName() : foundProduct.getProductName());
        foundProduct.setQuantity(productUpdateRequest.getProductQuantity() != 0
                ? productUpdateRequest.getProductQuantity() : foundProduct.getQuantity());
        foundProduct.setUnitPrice(productUpdateRequest.getUnitPrice() != null ?
                productUpdateRequest.getUnitPrice() : foundProduct.getUnitPrice());
    }

    private void findVendor(Integer userId) {
        vendorRepository.findById(userId)
                .orElseThrow(() -> new StoreException("Vendor isn't available"));
    }

    @Override
    public StoreResponse deleteProduct(Integer id, int productId) {
        findVendor(id);
        productRepository.deleteById(productId);
        return new StoreResponse("Product has been deleted");
    }


}
