package net.mikelythgoe.springforgraphql.api.response;

import java.util.Date;

public record ProductPriceHistory(String id,
                                  Date startDate,
                                  int price) {

}
