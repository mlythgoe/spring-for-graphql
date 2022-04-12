https://www.youtube.com/watch?v=6WfxWfIwoAU

http://localhost:8080/graphiql?path=/graphql

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

