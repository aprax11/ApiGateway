package com.example.ApiGateway.port;

import com.example.ApiGateway.core.domain.model.MessageType;
import com.example.ApiGateway.core.domain.model.Product;
import com.example.ApiGateway.exceptions.ErrorResponseException;
import com.example.ApiGateway.port.producer.ProductProducer;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.example.ApiGateway.core.domain.model.MessageType.GET_PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductProducerTest {

    public static final String TEST = "test";
    public static final String TEST_PRODUCT_ONE = "Ring";
    public static final String TEST_PRODUCT_TWO = "Kette";
    public static final String TEST_ID = UUID.randomUUID().toString();
    public static final String ROUTING_KEY = "routingKey";

    public static final List<Product> ALL_PRODUCTS = List.of(
            new Product(
                    UUID.randomUUID(),
                    TEST_PRODUCT_ONE,
                    "Das ist ein Ring.",
                    "22",
                    "details",
                    1,
                    "link"
            ),
            new Product(
                    UUID.randomUUID(),
                    TEST_PRODUCT_TWO,
                    "Das ist eine Kette.",
                    "220",
                    "details",
                    1,
                    "link"
            )
    );
    @InjectMocks
    private ProductProducer productProducer;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private DirectExchange directExchange;

    @BeforeEach
    void setUp() {
        try {
            var field = productProducer.getClass().getDeclaredField("routingKey");
            field.setAccessible(true);
            field.set(productProducer, ROUTING_KEY);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
    }
    @Test
    void getAllProductsEmptyResponseMessageTest() {
        when(directExchange.getName()).thenReturn(TEST);

        assertThrows(ErrorResponseException.class, () ->
                productProducer.sendGetAllProductsMessage());

    }
    @Test
    void getAllProductsTest() {
        when(directExchange.getName()).thenReturn(TEST);
        when(rabbitTemplate.sendAndReceive(eq(TEST), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(ALL_PRODUCTS)).getBytes()));

        var receivedResponse = productProducer.sendGetAllProductsMessage();

        assertThat(receivedResponse.get(0).getName()).isEqualTo(TEST_PRODUCT_ONE);
        assertThat(receivedResponse.get(1).getName()).isEqualTo(TEST_PRODUCT_TWO);
    }
    @Test
    void getProductEmptyResponseMessageTest() {
        when(directExchange.getName()).thenReturn(TEST);

        assertThrows(ErrorResponseException.class, () ->
                productProducer.sendGetProductMessage(TEST_ID));
    }
    @Test
    void getProductTest() {

        when(directExchange.getName()).thenReturn(TEST);
        when(rabbitTemplate.sendAndReceive(eq(TEST), eq(ROUTING_KEY), any(Message.class)))
                .thenReturn(new Message((new Gson().toJson(ALL_PRODUCTS.get(0))).getBytes()));


        var receivedResponse = productProducer.sendGetProductMessage(TEST_ID);

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate).sendAndReceive(anyString(), anyString(), argumentCaptor.capture());

        Message capturedArgument = argumentCaptor.getValue();

        String capturedArgumentBody = new String(capturedArgument.getBody(), StandardCharsets.UTF_8);

        MessageType capturedArgumentMessageType = MessageType.valueOf(capturedArgument
                .getMessageProperties()
                .getType());

        assertThat(capturedArgumentBody).isEqualTo(TEST_ID);

        assertThat(capturedArgumentMessageType).isEqualTo(GET_PRODUCT);

        assertThat(receivedResponse.getName()).isEqualTo(TEST_PRODUCT_ONE);
    }
}
