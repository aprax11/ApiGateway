package com.example.ApiGateway.core.domain.service;

import com.example.ApiGateway.core.domain.model.Product;
import com.example.ApiGateway.core.domain.service.ApiService;
import com.example.ApiGateway.port.producer.BasketProducer;
import com.example.ApiGateway.port.producer.ProductProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ApiServiceTest {

    @Mock
    ProductProducer productProducer;
    @Mock
    BasketProducer basketProducer;
    @Mock
    Product product;
    @InjectMocks
    ApiService apiService;

    @Test
    void getAllProductsTest() {
        apiService.getAllProducts();

        verify(productProducer).sendGetAllProductsMessage();
    }
    @Test
    void getProductTest() {
        UUID id = UUID.randomUUID();

        apiService.getProduct(id.toString());

        verify(productProducer).sendGetProductMessage(id.toString());
    }
    @Test
    void deleteProductTest() {
        UUID id = UUID.randomUUID();

        apiService.deleteProduct(id.toString());

        verify(productProducer).sendDeleteProductMessage(id.toString());
    }
    @Test
    void createProductTest() {

        apiService.createProduct(product);

        verify(productProducer).sendCreateProductRequest(product);
    }
    @Test
    void updateProductTest() {

        apiService.updateProduct(product);

        verify(productProducer).sendUpdateProductMessage(product);
    }
}
