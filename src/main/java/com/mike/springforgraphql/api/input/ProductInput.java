package com.mike.springforgraphql.api.input;

import java.util.List;

public record ProductInput(Long id, String title, String desc, Integer price, List<ProductPriceHistoryInput> productPriceHistoryInputList) {

}