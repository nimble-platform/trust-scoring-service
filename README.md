# Trust scoring and ranking service
**ServiceID**: trust-service

Trust computation service in the NIMBLE Microservice Infrastructure.

A prototype implementation of a NIMBLE trust computation service. The service will be able to compute a trust score for NIMBLE entities (products, services, companies) by evaluating their trust-related characteristics in regards to the trust expectations (criteria) of a particular B2B context. 

## Development

Micro-service implementation using the Spring Boot framework and Java language.
  
## Configuration

Base configuration can be found at src/main/resources/application.properties and bootstrap.yml.
[Spring Cloud Config](https://cloud.spring.io/spring-cloud-config/) is used for central configuration management. A central configuration is hosted on [https://github.com/nimble-platform/cloud-config](https://github.com/nimble-platform/cloud-config)
and injected during startup.

For database configuration, you will need to set your username and password directly in the BridgeDB java class.
However, we plan to refactor this part very soon and to provide database configuration in the  bootstrap.yml, application.properties as suggested by Spring Boot framework.

## Swagger

The Trust Scoring and Ranking API is designed using the [swagger.io editor](http://editor.swagger.io) (file: src/main/resources/api.yml) and the code generator for the Spring framework. 
The Maven plugin (swagger-codegen-maven-plugin) is used to generate defined interfaces automatically in each Maven build.

## How-to

### Service build and startup

#### local (non-cloud)
 ```
 mvn clean spring-boot:run
 ```
 
#### docker (cloud)
 It will be possible to run the service in docker environment from core cloud infrastructure, but this part is still under configuration.
 
### Get Version Request
 ```
 get
 curl http://localhost:8081/version
  ```
 ---
 
### Swagger endpoint 
 ```
	http://localhost:8081/swagger-ui.html
 ```
 
### Example Calls

Example calls will be added soon.

 ---
The project leading to this application has received funding from the European Union’s Horizon 2020 research and innovation programme under grant agreement No 723810.