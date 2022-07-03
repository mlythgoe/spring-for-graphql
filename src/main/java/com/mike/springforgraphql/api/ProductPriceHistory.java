package com.mike.springforgraphql.api;

import java.util.Date;

public record ProductPriceHistory(Long id, Date startDate, int price) {

}
