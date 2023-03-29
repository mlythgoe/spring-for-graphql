# Spring for GraphQL

When the application is running, you can use GraphiQL as a web based GraphiQL client

GraphiQL url - http://localhost:8080/graphiql?path=/graphql

... and you can use H2 Console as a web based DB Client

H2 Console URL - http://localhost:8080/h2-console/

## H2 DB Console Settings

* Setting Name: Generic H2 (Embedded)
* Driver class: org.h2.Driver
* JDBC URL - jdbc:h2:mem:springforgraphqldb
* Username: sa
* Password: password

## Using Postman

You can use any GraphQL client but the Git repo includes a Postman collection with
example queries and mutations - Note subscriptions do not seem to be working in Postman

## Subscriptions

http://localhost:8080/graphiql?path=/graphql&wsPath=/graphql

subscription {
    notifyProductPriceChange(productId:3) {
        id,
        startDate,
        price
    }
}

## GraphQL Resources

https://graphql.org/learn/ - An introduction to GraphQL, but a very thorough and detailed one.

https://www.apollographql.com/docs/ - Apollo is a Node/JavaScript framework for GraphQL. Very detailed, talks about 
    schema stitching and many more advanced topics as well as providing a good introduction.

https://spring.io/projects/spring-graphql#learn - Spring for GraphQL Documentation.



