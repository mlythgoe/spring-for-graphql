package net.mikelythgoe.springforgraphql.api.input;

import java.util.List;

public record ProductInput(String id,
                           String title,
                           String desc,
                           Integer price,
                           List<ProductPriceHistoryInput> productPriceHistoryInputList) {}