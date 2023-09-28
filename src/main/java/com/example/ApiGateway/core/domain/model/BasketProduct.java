package com.example.ApiGateway.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketProduct {

    private UUID id;

    private String name;

    private String price;

    private String image;

}
