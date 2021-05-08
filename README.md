# example-card-api
> Demo Spring Boot Rest API, showing how to use some useful Spring features.

## Included features
- In memory database using H2, using JPA for ORM.
- A RESTful API, with CRUD operations for handling users and cards.
- API includes allowing users to request cards, validating them, and generating image representations of them.
    - *(note cards are intentionally missing some CRUD operations, as they are treated as immutable)*
- Strong tests using functionality such as MockMVC for testing endpoints,
 and ApplicationContextRunner for tests involving Spring Context.
- Also includes Swagger UI setup to view endpoints.