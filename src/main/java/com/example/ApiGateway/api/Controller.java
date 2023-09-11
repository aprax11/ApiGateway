package com.example.ApiGateway.api;

import com.example.ApiGateway.core.domain.interfaces.IApiService;
import com.example.ApiGateway.core.domain.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path= "test/test")
@Slf4j
public class Controller {

    private final IApiService apiService;

    public Controller(IApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return apiService.getAllProducts();
    }

    @PostMapping()
    public void createProduct(@RequestBody Product product) {

        log.info("post Product for product {} Endpoint called", product.getName());
        apiService.createProduct(product);
    }

}
