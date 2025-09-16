package net.mikelythgoe.springforgraphql.subscription;

import net.mikelythgoe.springforgraphql.api.response.ProductPriceHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.client.WebSocketGraphQlClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.CountDownLatch;

public class ProductPriceNotificationClient {

    private static final Logger logger = LoggerFactory.getLogger(ProductPriceNotificationClient.class);
    private WebSocketGraphQlClient graphQlClient;
    private final DateFormat simpleDateFormat;

    public ProductPriceNotificationClient() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    }

    /**
     * Connect to the GraphQL WebSocket endpoint
     */
    public Mono<Void> connect(String graphqlEndpoint) {
        WebSocketClient socketClient = new ReactorNettyWebSocketClient();

        this.graphQlClient = WebSocketGraphQlClient.builder(URI.create(graphqlEndpoint), socketClient)
                .build();

        // Start the connection
        return this.graphQlClient.start();
    }

    /**
     * Subscribe to product price changes for a specific product
     */
    public Flux<ProductPriceHistory> subscribeToProductPriceChanges(Long productId) {
        if (graphQlClient == null) {
            return Flux.error(new IllegalStateException("Client not connected. Call connect() first."));
        }

        String subscription = """
                subscription NotifyProductPriceChange {
                    notifyProductPriceChange(productId: 1) {
                        id
                        startDate
                        price
                    }
                }
                
                """;

        return graphQlClient
                .document(subscription)
                .variable("productId", productId)
                .executeSubscription()
                .<ProductPriceHistory>handle((response, sink) -> {
                    if (!response.isValid()) {
                        logger.error("GraphQL errors: {}", response.getErrors());
                        sink.error(new RuntimeException("GraphQL subscription error: " + response.getErrors()));
                        return;
                    }

                    var responseField = response.field("notifyProductPriceChange");

                    var responseFieldValue = responseField.getValue();
                    assert responseFieldValue != null;

                    var id = String.valueOf(((LinkedHashMap<?, ?>) responseFieldValue).get("id").toString());

                    String startDateString = (String) ((LinkedHashMap<?, ?>) responseFieldValue).get("startDate");

                    Date startDate;
                    try {
                        startDate = simpleDateFormat.parse(startDateString);
                    } catch (ParseException e) {
                        sink.error(new RuntimeException(e));
                        return;
                    }

                    var price = (Integer) ((LinkedHashMap<?, ?>) responseFieldValue).get("price");

                    sink.next(new ProductPriceHistory(id, startDate, price));
                })
                .doOnNext(priceHistory -> logger.info("Received price change: {}", priceHistory))
                .doOnError(error -> logger.error("Subscription error: ", error))
                .doOnComplete(() -> logger.info("Subscription completed"));
    }


    /**
     * Subscribe and print responses to the console
     */
    public Disposable subscribeAndPrint(Long productId) {
        return subscribeToProductPriceChanges(productId)
                .subscribe(
                        priceHistory -> {
                            System.out.println("\n=== PRICE CHANGE NOTIFICATION ===");
                            System.out.printf("Product ID: %d%n", priceHistory.id());
                            System.out.printf("Product StartDate: %s%n", priceHistory.startDate());
                            System.out.printf("Product Price: %d%n", priceHistory.price());
                            System.out.println("================================\n");
                        },
                        error -> {
                            System.err.println("Subscription error: " + error.getMessage());
                            logger.error("Detailed error: ", error);
                        },
                        () -> System.out.println("Subscription completed")
                );
    }

    /**
     * Close the GraphQL client connection
     */
    public Mono<Void> close() {
        if (graphQlClient != null) {
            return graphQlClient.stop();
        }
        return Mono.empty();
    }

    // Example usage with proper connection handling
    public static void main(String[] args) {
        String endpoint = "ws://localhost:8080/graphql";
        Long productId = 1L;

        ProductPriceNotificationClient client = new ProductPriceNotificationClient();
        CountDownLatch latch = new CountDownLatch(1);

        try {
            // Connect first
            client.connect(endpoint)
                    .doOnSuccess(_ -> System.out.println("Connected to GraphQL endpoint"))
                    .doOnError(error -> {
                        System.err.println("Failed to connect: " + error.getMessage());
                        latch.countDown();
                    })
                    .then(Mono.fromRunnable(() -> {
                        System.out.println("Starting subscription for product " + productId + "...");

                        // Subscribe and print responses
                        Disposable subscription = client.subscribeAndPrint(productId);

                        // Set up the shutdown hook
                        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                            System.out.println("Shutting down...");
                            subscription.dispose();
                            client.close().block(Duration.ofSeconds(5));
                            latch.countDown();
                        }));

                        System.out.println("Listening for price changes... Press Ctrl+C to stop");
                    }))
                    .subscribe(
                            null,
                            error -> {
                                System.err.println("Connection error: " + error.getMessage());
                                latch.countDown();
                            }
                    );

            // Wait for shutdown
            latch.await();

        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        }
    }


}