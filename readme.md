# Spring for GraphQL

When the application is running, you can use GraphiQL as a web-based GraphQL client

GraphiQL url - http://localhost:8080/graphiql?path=/graphql

OR you can use Postman, which can be downloaded at: https://www.postman.com/downloads/

_I highly recommend using Postman_


... and you can use H2 Console as a web-based DB Client

H2 Console URL - http://localhost:8080/h2-console/

## H2 DB Console Settings

* Setting Name: Generic H2 (Embedded)
* Driver class: org.h2.Driver
* JDBC URL - jdbc:h2:mem:springforgraphqldb
* Username: sa
* Password: password

## Using Postman

You can use any GraphQL client to create API calls, but the Git repo includes a Postman collection with
example queries and mutations. Note that this collection is an older collection, created using a version of Postman
that didn't directly support GraphQL, so it was just using the standard HTTP features of Postman.

Postman can now handle graphql requests as real graphql requests, not just http requests.
Note: at the current time, you cannot export a graphql collection!!!!

To create GraphQL requests, you need to click 'file → new → graphql'.
you either need to do this when the code is running so postman can introspect the graphql schema, or you 
can import the graphql schema file.

When you save a graphql request, it must be in a collection without http requests. 
graphql requests and http request must be in separate collections!

When you click on 'add request' in a graphql collection, paste in the API URL (http://localhost:8080/graphql for this
application) and then the query tab will show all operations, i.e. it will show the introspected schema.
You can select an operation to build as a template, and you can add multiple queries in the same request, as
GraphQL allows.

Other than the strange situation where you cannot export a GraphQL collection, Postman is an excellent tool
for testing GraphQL APIs.

### Subscriptions

Most client applications currently struggle with subscriptions. GraphiQL provides a 
GraphQL client that supports subscriptions. 
Postman handles subscriptions in much the same way as other requests. 

Click on the Subscription operation you want to script. 
You can select or deselect the attributes you want in the response 

## Using GraphiQL

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

### Open GraphiQL when this application is running

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

The code has a subscription endpoint that generates new (random) prices for a product every second. 

## GraphQL Resources

https://graphql.org/learn/: An introduction to GraphQL, but a very thorough and detailed one.

https://www.apollographql.com/docs/: Apollo is a Node/JavaScript framework for GraphQL. Very detailed, talks about 
    schema stitching and many more advanced topics as well as providing a good introduction.

https://spring.io/projects/spring-graphql#learn: Spring for GraphQL Documentation.



