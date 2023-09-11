package com.example.ApiGateway.port.producer;

import com.example.ApiGateway.core.domain.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

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


    public void sendMessage(Product product){

        var message = new Message(new Gson().toJson(product).getBytes());
        setMessageType(message, CREATE_PRODUCT.name());
        log.info(String.format("Message sent -> %s", message));
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKey,
                message
        );
    }
    public List<Product> sendGetAllProductsMessage(){

        var message = new Message("".getBytes());
        setMessageType(message, OBTAIN_ALL_PRODUCTS.name());
        var receivedMessage = rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKey,
                message
        );
        if(receivedMessage == null || receivedMessage.getBody() == null){
            log.error("Body is null");
            return null;
        }
        return new Gson().fromJson(
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8),
                new TypeToken<List<Product>>() {
                }.getType()
        );
    }

    private void setMessageType(Message message, String type) {
        message.getMessageProperties()
                .setType(type);
    }
}
