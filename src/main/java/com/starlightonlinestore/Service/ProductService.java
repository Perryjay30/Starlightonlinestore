package com.starlightonlinestore.Service;

import com.starlightonlinestore.Data.Models.Product;
import com.starlightonlinestore.Data.dto.Request.AddProductRequest;
import com.starlightonlinestore.Data.dto.Request.ProductUpdateRequest;
import com.starlightonlinestore.Data.dto.Response.AddProductResponse;
import com.starlightonlinestore.Data.dto.Response.Response;

public interface ProductService {
//    Should there be a product model?
//    Shouldn't an admin/vendor be doing this?
    AddProductResponse createProduct(AddProductRequest addProductRequest);
    Product getProductById(int id);
    Response updateProduct
            (ProductUpdateRequest productUpdateRequest);

    Response deleteProduct(int id);




}
