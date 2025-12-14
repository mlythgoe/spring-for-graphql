package net.mikelythgoe.springforgraphql.api.response;

import java.time.Instant;

public record ProductPriceHistory(String id,
                                  Instant startDate,
                                  int price) {

}
