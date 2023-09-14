package com.example.ApiGateway.api;

import com.example.ApiGateway.core.domain.model.Product;
import com.example.ApiGateway.core.domain.service.ApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Mock
    private ApiService apiService;
    @Mock
    private Product product;
    @InjectMocks
    private Controller controller;

    @Test
    void getAllProductsTest() {

        controller.getAllProducts();

        verify(apiService).getAllProducts();
    }
    @Test
    void getProductTest() {
        UUID id = UUID.randomUUID();

        controller.getProduct(id.toString());

        verify(apiService).getProduct(id.toString());
    }
    @Test
    void updateProductTest() {

        controller.updateProduct(product);

        verify(apiService).updateProduct(product);
    }
    @Test
    void deleteProductTest() {
        UUID id = UUID.randomUUID();

        controller.deleteProduct(id.toString());

        verify(apiService).deleteProduct(id.toString());
    }
    @Test
    void createProductTest() {

        controller.createProduct(product);

        verify(apiService).createProduct(product);
    }
}
