package com.example.ApiGateway.core.domain.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private UUID id;

    private String name;

    private String description;

    private String price;

    private String details;

    private String imageLink;
}

