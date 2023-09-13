package com.example.ApiGateway.core.domain.interfaces;

import com.example.ApiGateway.core.domain.model.Product;
import com.example.ApiGateway.exceptions.ErrorResponseException;

import java.util.List;
import java.util.UUID;

public interface IApiService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Product getProduct(UUID id);
    Product updateProduct(Product product) throws ErrorResponseException;
    String deleteProduct(UUID id) throws ErrorResponseException;
}
