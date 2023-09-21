package com.example.ApiGateway.api;

import com.example.ApiGateway.core.domain.interfaces.IApiService;
import com.example.ApiGateway.core.domain.model.Product;
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
    public ResponseEntity<List<Product>> getAllProducts(){
        log.info("Endpoint: get all Products called");
        return status(OK).body(apiService.getAllProducts());
    }

    @GetMapping("/product/{id}")
    @ResponseStatus(OK)
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id){

        log.info("Endpoint: get Product for product {} called", id);
        return status(OK).body(apiService.getProduct(id));
    }

    @PostMapping(path="/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {

        log.info("Endpoint: post Product for product {} called", product.getName());
        return status(CREATED).body(apiService.createProduct(product));
    }
    @PutMapping(path = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {

        log.info("Endpoint: update Product for product {} called", product.getId());
        return status(OK).body(apiService.updateProduct(product));
    }
    @DeleteMapping("/products/{id}")
    @ResponseStatus(OK)
    public ResponseEntity<String> deleteProduct(@PathVariable("id") String id) {

        log.info("Endpoint: get delete product with id {} called", id);
        return status(OK).body(apiService.deleteProduct(id));
    }

}
