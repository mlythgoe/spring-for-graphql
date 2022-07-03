package com.mike.springforgraphql.api;

import com.mike.springforgraphql.model.Product;

import java.util.Date;

public record ProductPriceHistory(Long id, Date startDate, int price) {

}
