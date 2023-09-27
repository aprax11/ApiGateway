package com.example.ApiGateway.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Basket {

    private UUID userId;

    private List<BasketItem> products;

    private int totalPrice;


}
