package com.starlightonlinestore.Service;

import com.starlightonlinestore.Data.Models.Product;
import com.starlightonlinestore.Data.Models.ProductCategory;
import com.starlightonlinestore.Data.Repository.ProductRepository;
import com.starlightonlinestore.Data.dto.Request.AddProductRequest;
import com.starlightonlinestore.Data.dto.Request.ProductUpdateRequest;
import com.starlightonlinestore.Data.dto.Response.AddProductResponse;
import com.starlightonlinestore.Data.dto.Response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public AddProductResponse addProduct(AddProductRequest addProductRequest) {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(addProductRequest.getPrice()));
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
    public Response updateProduct(ProductUpdateRequest productUpdateRequest) {
        var foundProduct = productRepository.findById(productUpdateRequest.getId());
        if(foundProduct.isEmpty()) throw new RuntimeException("Product cannot be found");
        Product replaceProduct = updatingProduct(productUpdateRequest);
        productRepository.save(replaceProduct);
        return new Response("Product update successful");
    }

    private Product updatingProduct(ProductUpdateRequest productUpdateRequest) {
        Product replaceProduct = new Product();
        replaceProduct.setId(productUpdateRequest.getId());
//        if statement
//        String.valueOf(ProductCategory.valueOf(productUpdateRequest.getCategory()));
//        String.valueOf(ProductCategory.valueOf(productUpdateRequest.getCategory()));
////        else
        replaceProduct.setCategory(
        (ProductCategory.valueOf(String.valueOf(productUpdateRequest.getCategory()))));
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
    public Response deleteProduct(int id) {
        productRepository.deleteById(id);
        return new Response("Product has been deleted");
    }


}
