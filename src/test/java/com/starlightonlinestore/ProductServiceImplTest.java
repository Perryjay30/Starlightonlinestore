package com.starlightonlinestore;

import com.starlightonlinestore.Data.Models.ProductCategory;
import com.starlightonlinestore.Data.dto.Request.AddProductRequest;
import com.starlightonlinestore.Data.dto.Response.AddProductResponse;
import com.starlightonlinestore.Data.dto.Request.ProductUpdateRequest;
import com.starlightonlinestore.Data.dto.Response.Response;
import com.starlightonlinestore.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;


import static com.starlightonlinestore.Data.Models.ProductCategory.GROCERIES;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    private AddProductRequest addProductRequest;
    private AddProductRequest addSecondProductRequest;

    @BeforeEach
    void setUp(){
        addProductRequest = new AddProductRequest();

        addProductRequest.setName("Versace, Turtle-neck");
        addProductRequest.setPrice(BigDecimal.valueOf(15000.00));
        addProductRequest.setCategory
                (ProductCategory.FASHION);
        addProductRequest.setProductQuantity(32);

        addSecondProductRequest = new AddProductRequest();
        addSecondProductRequest.setName("Bag Of Rice");
        addSecondProductRequest.setPrice(BigDecimal.valueOf(45000.00));
        addSecondProductRequest.setCategory
                (GROCERIES);
        addSecondProductRequest.setProductQuantity(21);
    }

    @Test
    void addProductTest() {
        AddProductResponse response =
                productService.createProduct(addProductRequest);
        AddProductResponse response2 =
                productService.createProduct(addSecondProductRequest);
        assertNotNull(response);
        System.out.println(response2);
        assertEquals("product added successfully", response.getMessage());
    }

    @Test
    void testThatProductCanBeUpdated() {
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setId(352);
        productUpdateRequest.setCategory(String.valueOf(ProductCategory.APPLIANCES));
        productUpdateRequest.setName("Standing Fan");
        productUpdateRequest.setPrice(BigDecimal.valueOf(24000.00));
        productUpdateRequest.setQuantity(20);
        Response response = productService.updateProduct(productUpdateRequest);
        System.out.println(response);
        assertEquals("Product update successful", response.getMessage());

    }

    @Test
    void testThatProductCanBeDeleted() {
        Response deleteResponse = productService.deleteProduct(52);
        System.out.println(deleteResponse);
        assertEquals("Product has been deleted", deleteResponse.getMessage());
    }



}