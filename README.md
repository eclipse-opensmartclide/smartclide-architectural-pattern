# smartclide-architectural-pattern

SmartCLIDE Architectural Patterns module is a REST API backend service implemented in Spring that serves the SmartCLIDE Frontend IDE to assist the users of the IDE with the selection of a suitable architectural pattern for their application as well as the setup of an appropriate Github project repository for their project.

The REST API offers the following endpoints:

## Supported Patterns

List the names of achitectural patterns that are supported by this API

### Request

GET https://api.dev.smartclide.eu/architectural-patterns/supported-patterns

### Response

Responses (application/json)

[
    "EVENT_DRIVEN",
    "LAYERED",
    "MICROKERNEL",
    "MICROSERVICES",
    "SERVICE_ORIENTED",
    "SPACE_BASED"
]

## Survey

Get the JSON object that contains the survey content

### Request

GET https://api.dev.smartclide.eu/architectural-patterns/survey

### Response

Responses (application/json)

Please see 
<a href="https://github.com/eclipse-opensmartclide/smartclide-architectural-pattern/blob/main/src/main/resources/jsonfiles/survey.json">file</a>.

## Evaluation

Evaluates the input received by the FrontEnd module after the user has completed the survey. The input is a list of survey item IDs.

### Request

POST https://api.dev.smartclide.eu/architectural-patterns/evaluation

Body: required

Example value:

["Q1_1","Q2_1","Q2_2","Q3_3","Q3_4","Q4_1","Q4_2","Q5_3"]
 
### Response

Responses (application/json) 

A JSON object of a list of patterns with their evaluation scores in terms of percentages corresponding to their calculated ranking.

{
    "EVENT_DRIVEN": 13, 
    "LAYERED": 6,
    "MICROKERNEL": 10,
    "MICROSERVICES": 31,
    "SERVICE_ORIENTED": 12,
    "SPACE_BASED": 12
}

## Application

### Request

POST  https://api.dev.smartclide.eu/architectural-patterns/application 

Query Parameters:

**framework**
Type: String

Description: This is the framework name that the user has selected within the Service Creation flow in Step 2 Service Details/Framework. The endpoint expects one of the following three valid parameters:

- Java_with_Spring_Boot_and_MySQL
- Node.js
- Python

**pattern**
Type: String

Description: This is pattern name that is selected by the user. The endpoint expects one of the six following valid arguments:

- Event-driven
- Layered
- Microkernel
- Microservices
- Service-oriented
- Space-based

**name**
Type: String

Description: This is project name that the user has entered within the Service Creation flow in Step 2 Service Details/Name. It is an optional parameter.
When not provided, by default an appropriate project name is created based on the project template used.

**visibility** 
Type: String

Description: This is visibility option that the user has selected within the Service Creation flow in Step 2 Service Details/Visibility. It is an optional parameter. Valid parameters are:

- 0 (meaning that the project visibility is public)
- 2 (meaning that the project visibility is private)

When not provided, the default value is 0. 

**Headers:**
- gitLabServerURL: the url of the Gitlab server where the new project will be created. E.g., https://gitlab.dev.smartclide.eu
- gitlabToken: <GITLAB_PAT> (personal access token with write access)

Sample request URL:
 
https://api.dev.smartclide.eu/architectural-patterns/application?framework=Python&pattern=Layered&name=testProject&visibility=2

### Response 

HTTP response codes:
- 200 OK (upon succussful creation of the Github project)
- 400 Bad Request
- 401 Unauthorized
- 500 Internal Server Error
