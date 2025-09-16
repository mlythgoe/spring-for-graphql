package net.mikelythgoe.springforgraphql.api.input;

public record ProductPriceHistoryInput(String id,
                                       String startDate,
                                       int price) {}
