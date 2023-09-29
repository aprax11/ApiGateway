package com.example.ApiGateway.api;

import com.example.ApiGateway.core.domain.interfaces.IApiService;
import com.example.ApiGateway.core.domain.model.Basket;
import com.example.ApiGateway.core.domain.model.BasketComponent;
import com.example.ApiGateway.core.domain.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@CrossOrigin
@RestController
@Slf4j
@Data
public class Controller {

    private final IApiService apiService;

    @GetMapping("/allProducts")
    @ResponseStatus(OK)
    @Operation(summary = "Get all Products from the Product-Database.")
    public ResponseEntity<List<Product>> getAllProducts(){
        log.info("Endpoint: get all Products called");
        return status(OK).body(apiService.getAllProducts());
    }

    @GetMapping("/product/{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Get the specified Product from the Product-Database.")
    public ResponseEntity<Product> getProduct(
            @Parameter(description = "ID of the Product.")
            @PathVariable("id") String id){

        log.info("Endpoint: get Product for product {} called", id);
        return status(OK).body(apiService.getProduct(id));
    }

    @PostMapping(path="/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a product.")
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "The product that should be created.")
            @RequestBody Product product) {

        log.info("Endpoint: post Product for product {} called", product.getName());
        return status(CREATED).body(apiService.createProduct(product));
    }
    @PutMapping(path = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @Operation(summary = "Create a product.")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "The updated version of the product.")
            @RequestBody Product product) {

        log.info("Endpoint: update Product for product {} called", product.getId());
        return status(OK).body(apiService.updateProduct(product));
    }
    @DeleteMapping("/products/{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Delete a product.")
    public ResponseEntity<String> deleteProduct(
            @Parameter(description = "The id of the product that should be Deleted.")
            @PathVariable("id") String id) {

        log.info("Endpoint: get delete product with id {} called", id);
        return status(OK).body(apiService.deleteProduct(id));
    }
    @GetMapping("/basket/{username}")
    @ResponseStatus(OK)
    @Operation(summary = "Get the current shopping basket of the user form the database.")
    public ResponseEntity<Basket> getBasketForUser(
            @Parameter(description = "The id of the user.")
            @PathVariable("username") String username){

        log.info("Endpoint: get Basket for Basket of user {} called", username);
        return status(OK).body(apiService.getBasketOfUser(username));
    }
    @PutMapping(path = "/basket/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @Operation(summary = "Add an item to the shopping basket.")
    public ResponseEntity<BasketComponent> addToBasket(
            @Parameter(description = "The basket component that should be added.")
            @RequestBody BasketComponent basketComponent) {

        log.info("Endpoint: add to basket for component {} called", basketComponent);
        return status(OK).body(apiService.addToBasket(basketComponent));
    }
    @PutMapping(path = "/basket/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @Operation(summary = "Remove an item to the shopping basket.")
    public ResponseEntity<BasketComponent> deleteFromBasket(
            @Parameter(description = "The basket component that should be removed.")
            @RequestBody BasketComponent basketComponent) {

        log.info("Endpoint: delete from basket for component {} called", basketComponent);
        return status(OK).body(apiService.deleteFromBasket(basketComponent));
    }
}
