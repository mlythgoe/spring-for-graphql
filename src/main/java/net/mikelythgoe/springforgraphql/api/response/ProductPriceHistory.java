package net.mikelythgoe.springforgraphql.api.response;

import java.util.Date;
import java.util.UUID;

public record ProductPriceHistory(String id,
                                  Date startDate,
                                  int price) {

}
