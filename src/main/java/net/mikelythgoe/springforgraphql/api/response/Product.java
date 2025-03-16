package net.mikelythgoe.springforgraphql.api.response;

import java.util.ArrayList;

public record Product(Long id,
                      String title,
                      String desc,
                      Integer price,
                      ArrayList<ProductPriceHistory> productPriceHistories) {

}