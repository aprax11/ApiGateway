package com.example.ApiGateway.core.domain.service;

import com.example.ApiGateway.core.domain.interfaces.IApiService;
import com.example.ApiGateway.core.domain.model.Product;
import com.example.ApiGateway.exceptions.ErrorResponseException;
import com.example.ApiGateway.port.producer.ProductProducer;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Data
public class ApiService implements IApiService {

    private final ProductProducer productProducer;
    @Override
    public Product createProduct(Product product) {
        return productProducer.sendCreateProductRequest(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productProducer.sendGetAllProductsMessage();
    }

    @Override
    public Product getProduct(UUID id) {
        return null;
    }

    @Override
    public Product updateProduct(Product product) throws ErrorResponseException {
        return null;
    }

    @Override
    public String deleteProduct(UUID id) throws ErrorResponseException {
        return null;
    }
}
