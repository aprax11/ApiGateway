package com.example.ApiGateway.port;

import com.example.ApiGateway.core.domain.model.Product;
import com.example.ApiGateway.exceptions.ErrorResponseException;
import com.example.ApiGateway.port.producer.ProductProducer;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ProductProducerTest {

    public static final String TEST = "test";
    public static final String TEST_PRODUCT_ONE = "Ring";
    public static final String TEST_PRODUCT_TWO = "Kette";
    public static final String TEST_USER = "testUser";
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
}
