package com.mike.springforgraphql.api;

import com.mike.springforgraphql.model.Product;

import java.util.Date;

public record ProductPriceHistoryInput(Long id, String startDate, int price) {

}
