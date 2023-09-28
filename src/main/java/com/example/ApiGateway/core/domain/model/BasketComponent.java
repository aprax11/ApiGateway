package com.example.ApiGateway.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketComponent {

     private String userId;

     private BasketProduct product;

     public UUID getProductID(){
          UUID ret = product.getId();
          return ret;
     }
}
