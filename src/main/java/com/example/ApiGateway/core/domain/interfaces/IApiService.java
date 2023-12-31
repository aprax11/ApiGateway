package com.example.ApiGateway.core.domain.interfaces;

import com.example.ApiGateway.core.domain.model.Basket;
import com.example.ApiGateway.core.domain.model.BasketComponent;
import com.example.ApiGateway.core.domain.model.Product;
import com.example.ApiGateway.exceptions.ErrorResponseException;

import java.util.List;

public interface IApiService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Product getProduct(String id);
    Product updateProduct(Product product) throws ErrorResponseException;
    String deleteProduct(String id) throws ErrorResponseException;

    Basket getBasketOfUser(String username);

    BasketComponent addToBasket(BasketComponent basketComponent);

    BasketComponent deleteFromBasket(BasketComponent basketComponent);
}
