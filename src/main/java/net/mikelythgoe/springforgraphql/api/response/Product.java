package net.mikelythgoe.springforgraphql.api.response;

import java.util.ArrayList;

public record Product(String id,
                      String title,
                      String desc,
                      Integer price,
                      ArrayList<ProductPriceHistory> productPriceHistories) {

}