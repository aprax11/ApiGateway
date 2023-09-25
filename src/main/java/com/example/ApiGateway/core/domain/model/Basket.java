package com.example.ApiGateway.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Basket {

    private String userName;

    private List<BasketProduct> products;

    private int totalPrice;


}
