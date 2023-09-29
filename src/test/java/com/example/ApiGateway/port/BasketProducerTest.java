package com.example.ApiGateway.port;

import com.example.ApiGateway.core.domain.model.*;
import com.example.ApiGateway.exceptions.ErrorResponseException;
import com.example.ApiGateway.port.producer.BasketProducer;
import com.example.ApiGateway.port.producer.MessageType;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static com.example.ApiGateway.port.ProductProducerTest.ALL_PRODUCTS;
import static com.example.ApiGateway.port.producer.MessageType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BasketProducerTest {
    public final String TEST = "test";
    public final String TEST_PRODUCT_NAME_ONE = "Ring";
    public final String TEST_PRODUCT_NAME_TWO = "Kette";
    public final UUID TEST_USER_ID = UUID.randomUUID();
    public final UUID TEST_ITEM_ID = UUID.randomUUID();
    public final String ROUTING_KEY = "routingKey";

    public final Basket TEST_BASKET = new Basket(TEST_USER_ID,
            List.of(new BasketItem(
                    TEST_ITEM_ID,
                    TEST_PRODUCT_NAME_ONE,
                    "22€",
                    "2",
                    2
                    )
            ),
            44
    );
    public final BasketComponent TEST_BASKET_COMPONENT = new BasketComponent(TEST_USER_ID.toString(),
            new BasketProduct(TEST_ITEM_ID,
                    TEST_PRODUCT_NAME_ONE,
                    "22€",
                    "2"
            )
    );


    @InjectMocks
    private BasketProducer basketProducer;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private DirectExchange directExchange;

    @BeforeEach
    void setUp() {
        try {
            var field = basketProducer.getClass().getDeclaredField("routingKeyBasketService");
            field.setAccessible(true);
            field.set(basketProducer, ROUTING_KEY);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
    }
    @Test
    void getBasketForUserEmptyResponseMessageTest() {

        when(directExchange.getName()).thenReturn(TEST);

        assertThrows(ErrorResponseException.class, () ->
                basketProducer.sendGetBasketMessage(TEST_USER_ID.toString()));
    }
    @Test
    void getBasketOfUserTest() {

        when(directExchange.getName()).thenReturn(TEST);
        when(rabbitTemplate.sendAndReceive(eq(TEST), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(TEST_BASKET)).getBytes()));

        Basket receivedResponse = basketProducer.sendGetBasketMessage(TEST_USER_ID.toString());

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate).sendAndReceive(anyString(), anyString(), argumentCaptor.capture());

        Message capturedArgument = argumentCaptor.getValue();

        String capturedArgumentBody = new String(capturedArgument.getBody(), StandardCharsets.UTF_8);

        MessageType capturedArgumentMessageType = MessageType.valueOf(capturedArgument
                .getMessageProperties()
                .getType());


        assertThat(capturedArgumentBody).isEqualTo(TEST_USER_ID.toString());
        assertThat(capturedArgumentMessageType).isEqualTo(GET_BASKET);
        assertThat(receivedResponse).isEqualTo(TEST_BASKET);
    }
    @Test
    void addToBasketEmptyResponseMessageTest() {

        when(directExchange.getName()).thenReturn(TEST);

        assertThrows(ErrorResponseException.class, () ->
                basketProducer.sendAddToBasketRequest(TEST_BASKET_COMPONENT));
    }
    @Test
    void addToBasketTest() {

        when(directExchange.getName()).thenReturn(TEST);
        when(rabbitTemplate.sendAndReceive(eq(TEST), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(TEST_BASKET_COMPONENT)).getBytes()));

        basketProducer.sendAddToBasketRequest(TEST_BASKET_COMPONENT);

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate).sendAndReceive(anyString(), anyString(), argumentCaptor.capture());

        Message capturedArgument = argumentCaptor.getValue();

        String capturedArgumentBody = new String(capturedArgument.getBody(), StandardCharsets.UTF_8);

        MessageType capturedArgumentMessageType = MessageType.valueOf(capturedArgument
                .getMessageProperties()
                .getType());


        BasketComponent argumentComponent = new Gson().fromJson(
                capturedArgumentBody,
                BasketComponent.class
        );
        assertThat(argumentComponent).isEqualTo(TEST_BASKET_COMPONENT);
        assertThat(capturedArgumentMessageType).isEqualTo(ADD_TO_BASKET);
    }
    @Test
    void deleteFromBasketEmptyResponseMessageTest() {

        when(directExchange.getName()).thenReturn(TEST);

        assertThrows(ErrorResponseException.class, () ->
                basketProducer.sendDeleteFromBasketMessage(TEST_BASKET_COMPONENT));
    }
    @Test
    void deleteFromBasketTest() {

        when(directExchange.getName()).thenReturn(TEST);
        when(rabbitTemplate.sendAndReceive(eq(TEST), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(TEST_BASKET_COMPONENT)).getBytes()));

        basketProducer.sendDeleteFromBasketMessage(TEST_BASKET_COMPONENT);

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate).sendAndReceive(anyString(), anyString(), argumentCaptor.capture());

        Message capturedArgument = argumentCaptor.getValue();

        String capturedArgumentBody = new String(capturedArgument.getBody(), StandardCharsets.UTF_8);

        MessageType capturedArgumentMessageType = MessageType.valueOf(capturedArgument
                .getMessageProperties()
                .getType());


        BasketComponent argumentComponent = new Gson().fromJson(
                capturedArgumentBody,
                BasketComponent.class
        );
        assertThat(argumentComponent).isEqualTo(TEST_BASKET_COMPONENT);
        assertThat(capturedArgumentMessageType).isEqualTo(DELETE_FROM_BASKET);
    }
}
