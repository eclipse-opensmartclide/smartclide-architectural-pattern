# smartclide-architectural-pattern

SmartCLIDE Architectural Patterns module is a REST API backend service implemented in Spring that serves the SmartCLIDE Frontend IDE to assist the users of the IDE with the selection of a suitable architectural pattern for their application as well as the setup of an appropriate GitHub project repository for their project.

The REST API offers the following endpoints:

## Supported Patterns

Lists the names of architectural patterns that are supported by this API.

### Request

GET https://api.dev.smartclide.eu/architectural-patterns/supported-patterns

### Response

Content-Type: `application/json`

```json
[
    "EVENT_DRIVEN",
    "LAYERED",
    "MICROKERNEL",
    "MICROSERVICES",
    "SERVICE_ORIENTED",
    "SPACE_BASED"
]
```

## Survey

Gets the JSON object that contains the survey content.

### Request

GET https://api.dev.smartclide.eu/architectural-patterns/survey

### Response

Content-Type: `application/json`

Please see [survey.json](src/main/resources/jsonfiles/survey.json).

## Evaluation

Evaluates the input received by the FrontEnd module after the user has completed the survey. The input is a list of survey item IDs.

### Request

POST https://api.dev.smartclide.eu/architectural-patterns/evaluation

Body: required

Content-Type: `application/json`

Example value:

```json
["Q1_1","Q2_1","Q2_2","Q3_3","Q3_4","Q4_1","Q4_2","Q5_3"]
```
 
### Response

Content-Type: `application/json`

A JSON object of a list of patterns with their evaluation scores in terms of percentages corresponding to their calculated ranking.

```json
{
    "EVENT_DRIVEN": 16, 
    "LAYERED": 35,
    "MICROKERNEL": 7,
    "MICROSERVICES": 13,
    "SERVICE_ORIENTED": 11,
    "SPACE_BASED": 17
}
```

## Application

Based on the query parameters described below, it chooses an appropriate GitHub project template, and returns the URL to the template repository.

### Request

POST  https://api.dev.smartclide.eu/architectural-patterns/application 

Query Parameters:

**framework**
Type: String

Description: This is the framework name that the user has selected within the Service Creation flow in Step 2 Service Details/Framework. The endpoint expects one of the following three valid parameters:

- Java_with_Spring_Boot_and_MySQL
- Nodejs
- Python

**pattern**
Type: String

Description: This is the pattern name that is selected by the user. The endpoint expects one of the six following valid parameters:

- EVENT_DRIVEN
- LAYERED
- MICROKERNEL
- MICROSERVICES
- SERVICE_ORIENTED 
- SPACE_BASED

Example POST request: https://api.dev.smartclide.eu/architectural-patterns/application?framework=Nodejs&pattern=EVENT_DRIVEN

### Response 

The template repository URL on GitHub, e.g., `https://github.com/che-samples/web-java-spring-boot`

Content-Type: `application/json`

```json
{
    "templateRepositoryUrl": "https://github.com/che-samples/web-java-spring-boot"
}
```

The following repository templates are being currently used:

For Java with Spring Boot and MySQL
 - Event-driven: https://github.com/horozal/event-driven-java-spring-boot-mysql
 - Layered: https://github.com/horozal/layered-architecture-java-spring-boot-mysql
 - Microkernel: https://github.com/horozal/microkernel-spring-boot-mysql
 - Microservices: https://github.com/horozal/microservices-java-spring-boot-mysql
 - Service-oriented: https://github.com/horozal/service-oriented-java-spring-boot-mysql
 - Space-based: https://github.com/horozal/space-based-spring-boot-mysql

For Node.js
 - [Event-driven](https://github.com/horozal/event-driven-nodejs)
 - [Layered](https://github.com/horozal/layered-architecture-nodejs)
 - Microkernel: https://github.com/horozal/microkernel-nodejs
 - Microservices: https://github.com/horozal/microservices-nodejs
 - Service-oriented: https://github.com/horozal/service-oriented-nodejs
 - Space-based: https://github.com/horozal/space-based-nodejs

For Python
 - Event-driven: https://github.com/horozal/event-driven-python
 - Layered: https://github.com/horozal/layered-architecture-python
 - Microkernel: https://github.com/horozal/microkernel-python
 - Microservices: https://github.com/horozal/microservices-python
 - Service-oriented: https://github.com/horozal/service-oriented-python
 - Space-based: https://github.com/horozal/space-based-python
