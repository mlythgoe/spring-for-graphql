package com.mike.springforgraphql.api;

public record ProductSearchCriteriaInput(String title, String desc, Integer lowerPrice, Integer upperPrice) {

}