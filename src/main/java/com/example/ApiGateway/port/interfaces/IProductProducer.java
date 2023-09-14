package com.example.ApiGateway.port.interfaces;

import com.example.ApiGateway.core.domain.model.Product;

import java.util.List;

public interface IProductProducer {
    Product sendCreateProductRequest(Product product);

    List<Product> sendGetAllProductsMessage();

    Product sendUpdateProductMessage(Product product);

    String sendDeleteProductMessage(String id);

    Product sendGetProductMessage(String id);
}
