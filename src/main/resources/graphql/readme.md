https://www.youtube.com/watch?v=6WfxWfIwoAU - Video this code is based on

http://localhost:8080/graphiql?path=/graphql - GraphiQL url

query GetProduct($productId:ID){
    getProduct(id:$productId){
        id
        title
        desc
    }
}

query variable declaration = 
{
    "productId": 1
}

-------------------------------------------------

query {
    allProducts{
        id
        title
        desc
    }
}