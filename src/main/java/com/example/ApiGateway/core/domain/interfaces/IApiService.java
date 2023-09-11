package com.example.ApiGateway.core.domain.interfaces;

import com.example.ApiGateway.core.domain.model.Product;

import java.util.List;

public interface IApiService {
    void createProduct(Product product);
    List<Product> getAllProducts();

}
