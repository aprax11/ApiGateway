package com.example.ApiGateway.port.interfaces;

import com.example.ApiGateway.core.domain.model.Basket;
import com.example.ApiGateway.core.domain.model.BasketComponent;

import java.util.UUID;

public interface IBasketProducer {
    BasketComponent sendAddToBasketRequest(BasketComponent basketComponent);

    Basket sendGetBasketMessage(String username);

    BasketComponent sendDeleteFromBasketMessage(BasketComponent basketComponent);
}
