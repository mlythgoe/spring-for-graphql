package net.mikelythgoe.springforgraphql.api.response;

import java.util.Date;

public record ProductPriceHistory(Long id,
                                  Date startDate,
                                  int price) {

}
