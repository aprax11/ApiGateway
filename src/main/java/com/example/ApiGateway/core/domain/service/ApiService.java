package com.example.ApiGateway.core.domain.service;

import com.example.ApiGateway.core.domain.interfaces.IApiService;
import com.example.ApiGateway.core.domain.model.Basket;
import com.example.ApiGateway.core.domain.model.BasketComponent;
import com.example.ApiGateway.core.domain.model.Product;
import com.example.ApiGateway.exceptions.ErrorResponseException;
import com.example.ApiGateway.port.interfaces.IBasketProducer;
import com.example.ApiGateway.port.interfaces.IProductProducer;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Data
public class ApiService implements IApiService {

    private final IProductProducer productProducer;

    private final IBasketProducer basketProducer;
    @Override
    public Product createProduct(Product product) {
        return productProducer.sendCreateProductRequest(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productProducer.sendGetAllProductsMessage();
    }

    @Override
    public Product getProduct(String id) {
        return productProducer.sendGetProductMessage(id);
    }

    @Override
    public Product updateProduct(Product product) throws ErrorResponseException {
        return productProducer.sendUpdateProductMessage(product);
    }
    @Override
    public String deleteProduct(String id) throws ErrorResponseException {
        return productProducer.sendDeleteProductMessage(id);
    }
    @Override
    public Basket getBasketOfUser(String username){
        return basketProducer.sendGetBasketMessage(username);
    }
    @Override
    public BasketComponent addToBasket(BasketComponent basketComponent){
        return basketProducer.sendAddToBasketRequest(basketComponent);
    }
    @Override
    public BasketComponent deleteFromBasket(BasketComponent basketComponent){
        return basketProducer.sendDeleteFromBasketMessage(basketComponent);
    }
}
