package com.starlightonlinestore.service;

import com.starlightonlinestore.data.models.Product;
import com.starlightonlinestore.data.models.ProductCategory;
import com.starlightonlinestore.data.repository.ProductRepository;
import com.starlightonlinestore.data.dto.Request.AddProductRequest;
import com.starlightonlinestore.data.dto.Request.ProductUpdateRequest;
import com.starlightonlinestore.data.dto.Response.AddProductResponse;
import com.starlightonlinestore.data.dto.Response.StoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public AddProductResponse createProduct(AddProductRequest addProductRequest) {
        Product product = new Product();
        product.setPrice(addProductRequest.getPrice());
        product.setCategory(ProductCategory.valueOf(String.valueOf(addProductRequest
                .getCategory())));
        product.setName(addProductRequest.getName());
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
    public StoreResponse updateProduct(Integer id, ProductUpdateRequest productUpdateRequest) {
        var foundProduct = productRepository.findById(id);
        if(foundProduct.isEmpty()) throw new RuntimeException("Product cannot be found");
        Product replaceProduct = updatingProduct(id, productUpdateRequest);
        productRepository.save(replaceProduct);
        return new StoreResponse("Product update successful");
    }

    private Product updatingProduct(Integer id, ProductUpdateRequest productUpdateRequest) {
        Product replaceProduct = new Product();
        replaceProduct.setId(id);
//        if statement
//        String.valueOf(ProductCategory.valueOf(productUpdateRequest.getCategory()));
//        String.valueOf(ProductCategory.valueOf(productUpdateRequest.getCategory()));
////        else
        replaceProduct.setCategory(productUpdateRequest.getCategory());
        updatingProduct2(productUpdateRequest, replaceProduct);
        return replaceProduct;
    }

    private void updatingProduct2(ProductUpdateRequest productUpdateRequest, Product replaceProduct) {
        replaceProduct.setName(productUpdateRequest.getName() != null && !productUpdateRequest.getName().equals("")
                ? productUpdateRequest.getName() : replaceProduct.getName());
        replaceProduct.setQuantity(productUpdateRequest.getQuantity() != 0
                ? productUpdateRequest.getQuantity() : replaceProduct.getQuantity());
        replaceProduct.setPrice(productUpdateRequest.getPrice() != null ?
                productUpdateRequest.getPrice() : replaceProduct.getPrice());
    }

    @Override
    public StoreResponse deleteProduct(int id) {
        productRepository.deleteById(id);
        return new StoreResponse("Product has been deleted");
    }


}
