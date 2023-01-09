package com.starlightonlinestore.service;

import com.starlightonlinestore.data.models.Product;
import com.starlightonlinestore.data.dto.Request.AddProductRequest;
import com.starlightonlinestore.data.dto.Request.ProductUpdateRequest;
import com.starlightonlinestore.data.dto.Response.AddProductResponse;
import com.starlightonlinestore.data.dto.Response.Response;

public interface ProductService {
//    Should there be a product model?
//    Shouldn't an admin/vendor be doing this?
    AddProductResponse createProduct(AddProductRequest addProductRequest);
    Product getProductById(int id);
    Response updateProduct
            (ProductUpdateRequest productUpdateRequest);

    Response deleteProduct(int id);




}
