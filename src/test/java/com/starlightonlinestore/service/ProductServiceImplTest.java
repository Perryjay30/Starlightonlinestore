package com.starlightonlinestore.service;

import com.starlightonlinestore.data.models.ProductCategory;
import com.starlightonlinestore.data.dto.Request.AddProductRequest;
import com.starlightonlinestore.data.dto.Response.AddProductResponse;
import com.starlightonlinestore.data.dto.Request.ProductUpdateRequest;
import com.starlightonlinestore.data.dto.Response.StoreResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static com.starlightonlinestore.data.models.ProductCategory.GROCERIES;
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
        addProductRequest.setPrice(15000.00);
        addProductRequest.setCategory
                (ProductCategory.FASHION);
        addProductRequest.setProductQuantity(32);

        addSecondProductRequest = new AddProductRequest();
        addSecondProductRequest.setName("Bag Of Rice");
        addSecondProductRequest.setPrice(45000.00);
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
        productUpdateRequest.setCategory(ProductCategory.APPLIANCES);
        productUpdateRequest.setName("Standing Fan");
        productUpdateRequest.setUnitPrice(24000.00);
        productUpdateRequest.setQuantity(20);
        StoreResponse response = productService.updateProduct(352, productUpdateRequest);
        System.out.println(response);
        assertEquals("Product update successful", response.getMessage());

    }

    @Test
    void testThatProductCanBeDeleted() {
        StoreResponse deleteResponse = productService.deleteProduct(352);
        System.out.println(deleteResponse);
        assertEquals("Product has been deleted", deleteResponse.getMessage());
    }



}