package com.mike.springforgraphql.api.input;

public record ProductSearchCriteriaInput(String title,
                                         String desc,
                                         Integer lowerPrice,
                                         Integer upperPrice) {}