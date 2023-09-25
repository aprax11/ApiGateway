package com.example.ApiGateway.port.producer;

import com.example.ApiGateway.core.domain.model.Basket;
import com.example.ApiGateway.core.domain.model.BasketComponent;
import com.example.ApiGateway.exceptions.ErrorResponseException;
import com.example.ApiGateway.port.interfaces.IBasketProducer;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import static com.example.ApiGateway.port.producer.MessageType.*;

@Service
@Slf4j
public class BasketProducer implements IBasketProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange directExchange;

    @Value("basket-service.rpc.key")
    private String routingKeyBasketService;



    @Override
    public BasketComponent sendAddToBasketRequest(BasketComponent basketComponent){

        byte[] serializedProduct = new Gson().toJson(basketComponent).getBytes();

        Message message = new Message(serializedProduct);
        setMessageType(message, ADDTOBASKET.name());

        Message receivedMessage = sendAndReceive(message);

        if (messageIsNull(receivedMessage)) {
            logErrorFor("getting Product "+basketComponent.getProductID());
            throw new ErrorResponseException("did not get product: "+basketComponent.getProductID());
        }

        String receivedObject = new String(receivedMessage.getBody(), StandardCharsets.UTF_8);
        return new Gson().fromJson(
                receivedObject,
                BasketComponent.class
        );
    }
    @Override
    public Basket sendGetBasketMessage(String username){

        Message message = new Message(username.getBytes());
        setMessageType(message, GET_BASKET.name());

        Message receivedMessage = sendAndReceive(message);

        if (messageIsNull(receivedMessage)) {
            logErrorFor("getting Basket for user: "+username);
            throw new ErrorResponseException("did not get basket");
        }

        String receivedObject = new String(receivedMessage.getBody(), StandardCharsets.UTF_8);
        return new Gson().fromJson(
                receivedObject,
                Basket.class
        );
    }
    @Override
    public BasketComponent sendDeleteFromBasketMessage(BasketComponent basketComponent){

        byte[] serializedProduct = new Gson().toJson(basketComponent).getBytes();

        Message message = new Message(serializedProduct);
        setMessageType(message, DELETEFROMBASKET.name());

        Message receivedMessage = sendAndReceive(message);

        if (messageIsNull(receivedMessage)) {
            logErrorFor("deleting from basket: "+basketComponent.getProductID());
            throw new ErrorResponseException("did not delete from Basket: "+basketComponent.getProductID());
        }

        String receivedObject = new String(receivedMessage.getBody(), StandardCharsets.UTF_8);
        return new Gson().fromJson(
                receivedObject,
                BasketComponent.class
        );
    }

    private Message sendAndReceive(Message message){
        Message receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKeyBasketService,
                message
        );
        return receivedMessage;
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
