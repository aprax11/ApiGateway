package com.example.ApiGateway.core.domain.service;

import com.example.ApiGateway.core.domain.interfaces.IApiService;
import com.example.ApiGateway.core.domain.model.Product;
import com.example.ApiGateway.port.producer.ProductProducer;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class ApiService implements IApiService {

    private final ProductProducer productProducer;
    @Override
    public void createProduct(Product product) {
        productProducer.sendMessage(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productProducer.sendGetAllProductsMessage();
    }
}
