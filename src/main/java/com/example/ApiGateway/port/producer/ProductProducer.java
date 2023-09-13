package com.example.ApiGateway.port.producer;

import com.example.ApiGateway.core.domain.model.Product;
import com.example.ApiGateway.exceptions.ErrorResponseException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static com.example.ApiGateway.core.domain.model.MessageType.*;

@Slf4j
@Service
public class ProductProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange directExchange;

    @Value("product-service.rpc.key")
    private String routingKey;


    public Product sendCreateProductRequest(Product product){

        byte[] serializedProduct = new Gson().toJson(product).getBytes();

        Message message = new Message(serializedProduct);
        setMessageType(message, CREATE_PRODUCT.name());

        Message receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKey,
                message
        );

        if (messageIsNull(receivedMessage)) {
            logErrorFor("getting Product "+product.getName());
            throw new ErrorResponseException("did not get product: "+product.getName());
        }

        String receivedObject = new String(receivedMessage.getBody(), StandardCharsets.UTF_8);
        return new Gson().fromJson(
                receivedObject,
                Product.class
        );
    }
    public List<Product> sendGetAllProductsMessage(){

        Message message = new Message("".getBytes());
        setMessageType(message, GET_ALL_PRODUCTS.name());
        Message receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKey,
                message
        );
        if (messageIsNull(receivedMessage)) {
            logErrorFor("getting all Products");
            throw new ErrorResponseException("did not get all products");
        }

        String receivedObject = new String(receivedMessage.getBody(), StandardCharsets.UTF_8);
        return new Gson().fromJson(
                receivedObject,
                new TypeToken<List<Product>>() {
                }.getType()
        );
    }
    public Product sendUpdateProductMessage(Product product){

        byte[] serializedProduct = new Gson().toJson(product).getBytes();

        Message message = new Message(serializedProduct);
        setMessageType(message, UPDATE_PRODUCT.name());
        Message receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKey,
                message
        );
        if (messageIsNull(receivedMessage)) {
            logErrorFor("updating Product "+product.getName());
            throw new ErrorResponseException("did not update product: "+product.getName());
        }

        String receivedObject = new String(receivedMessage.getBody(), StandardCharsets.UTF_8);
        return new Gson().fromJson(
                receivedObject,
                Product.class
        );
    }
    public String sendDeleteProductMessage(String id){

        Message message = new Message(id.getBytes());
        setMessageType(message, DELETE_PRODUCT.name());
        Message receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKey,
                message
        );
        if (messageIsNull(receivedMessage)) {
            logErrorFor("deleting Product "+id);
            throw new ErrorResponseException("did not delete product: "+id);
        }

        return new String(receivedMessage.getBody(), StandardCharsets.UTF_8);
    }
    public Product sendGetProductMessage(String id){

        Message message = new Message(id.getBytes());
        setMessageType(message, GET_PRODUCT.name());
        Message receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKey,
                message
        );
        if (messageIsNull(receivedMessage)) {
            logErrorFor("getting Product "+id);
            throw new ErrorResponseException("did not get product: "+id);
        }

        String receivedObject = new String(receivedMessage.getBody(), StandardCharsets.UTF_8);
        return new Gson().fromJson(
                receivedObject,
                Product.class
        );
    }

    private void setMessageType(Message message, String type) {
        message.getMessageProperties()
                .setType(type);
    }
    private boolean messageIsNull(Message receivedMessage) {
        return receivedMessage == null ||
                receivedMessage.getBody() == null ||
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8).equals("errorResponse");
    }
    private void logErrorFor(String taskName) {
        log.error("error while '{}', because received Message from Product Service via rabbitmq is empty", taskName);
    }
}
