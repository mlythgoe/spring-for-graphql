package net.mikelythgoe.springforgraphql.api.input;

public record ProductPriceHistoryInput(Long id,
                                       String startDate,
                                       int price) {}
