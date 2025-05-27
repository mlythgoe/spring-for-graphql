# Spring for GraphQL

When the application is running, you can use GraphiQL as a web-based GraphQL client

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

You can use any GraphQL client, but the Git repo includes a Postman collection with
example queries and mutations. Note subscriptions do NOT seem to be working in Postman

## Subscriptions

Most client applications currently struggle with subscriptions. GraphiQL provides a 
GraphQL client that supports subscriptions.

>GraphiQL is a graphical interactive in-browser GraphQL IDE. 
>It is very popular amongst developers as it makes it easy to explore and interactively 
>develop GraphQL APIs. 
>During development, a stock GraphiQL integration is often enough to help developers 
>work on an API. In production, applications can require a custom GraphiQL build, 
>that ships with a company logo or specific authentication support.

>Spring for GraphQL ships with a stock GraphiQL index.html page that uses 
>static resources hosted on the esm.sh CDN. Spring Boot applications can easily 
>enable this page with a configuration property.

From - https://docs.spring.io/spring-graphql/reference/graphiql.html

## Open GraphiQL when this application is running

Open a browser window/tab and navigate to:

```
http://localhost:8080/graphiql?path=/graphql&wsPath=/graphql
```

Paste the following query into the GraphiQL window:

```
subscription {
    notifyProductPriceChange(productId:3) {
        id,
        startDate,
        price
    }
}
```

The code has a subscription endpoint that generates new (random) prices for a product every 10 seconds. 

## GraphQL Resources

https://graphql.org/learn/ - An introduction to GraphQL, but a very thorough and detailed one.

https://www.apollographql.com/docs/ - Apollo is a Node/JavaScript framework for GraphQL. Very detailed, talks about 
    schema stitching and many more advanced topics as well as providing a good introduction.

https://spring.io/projects/spring-graphql#learn - Spring for GraphQL Documentation.



