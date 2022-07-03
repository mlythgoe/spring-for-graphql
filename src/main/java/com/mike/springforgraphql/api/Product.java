package com.mike.springforgraphql.api;

import java.util.ArrayList;

public record Product(Long id, String title, String desc, Integer price, ArrayList<ProductPriceHistory> productPriceHistories) {

}