package com.starlightonlinestore.service;

import com.starlightonlinestore.data.models.Product;
import com.starlightonlinestore.data.dto.Request.AddProductRequest;
import com.starlightonlinestore.data.dto.Request.ProductUpdateRequest;
import com.starlightonlinestore.data.dto.Response.AddProductResponse;
import com.starlightonlinestore.data.dto.Response.StoreResponse;

import java.util.List;

public interface ProductService {
//    Should there be a product model?
//    Shouldn't an admin/vendor be doing this?
    AddProductResponse createProduct(AddProductRequest addProductRequest);
    Product getProductById(int id);

    List<Product> viewAllProduct(Integer customerId);


    StoreResponse updateProduct
            (Integer vendorId, Integer productId, ProductUpdateRequest productUpdateRequest);

    StoreResponse deleteProduct(Integer id, int productId);




}
