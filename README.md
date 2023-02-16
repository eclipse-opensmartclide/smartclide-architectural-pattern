# smartclide-architectural-pattern

1) GET https://api.dev.smartclide.eu/architectural-patterns/supported-patterns

List the names of achitectural patterns that are supported by this API

Responses (application/json)
[
    "EVENT_DRIVEN",
    "LAYERED",
    "MICROKERNEL",
    "MICROSERVICES",
    "SERVICE_ORIENTED",
    "SPACE_BASED"
]

2) GET https://api.dev.smartclide.eu/architectural-patterns/survey

Get the JSON object that contains the survey content

Responses (application/json)

Please see file

3) POST https://api.dev.smartclide.eu/architectural-patterns/evaluation

Evaluate the user input from the survey

Body: required

Example value:

["Q1_1","Q2_1","Q2_2","Q3_3","Q3_4","Q4_1","Q4_2","Q5_3"]
 
Responses (application/json) 

A JSON object of a list of patterns with their evaluation scores in terms of percentages corresponding to their calculated ranking.

{
    "SERVICE_ORIENTED": 13, 
    "SPACE_BASED": 6,
    "MICROSERVICES": 10,
    "LAYERED": 31,
    "EVENT_DRIVEN": 12,
    "MICROKERNEL": 12
}

4) POST  https://api.dev.smartclide.eu/architectural-patterns/application 

Query Parameters:

**framework**
Type: String

Description: This is the framework name that the user has selected within the Service Creation flow in Step 2 Service Details/Framework. The endpoint expects one of the following three valid parameters:

- Java_with_Spring_Boot_and_MySQL
- Node.js
- Python

**pattern**
Type: String

Description: This is pattern name that is selected by the user. The endpoint expects one of the six following valid parameters:

- Layered
- Event-driven
- Microservices
- Microkernel
- Service-oriented
- Space-based

**name**
Type: String

Description: This is project name that the used has entered within the Service Creation flow in Step 2 Service Details/Name. It is an optional parameter.

**visibility** 
Type: String

Description: This is visibility option that the used has selected within the Service Creation flow in Step 2 Service Details/Visibility. It is an optional parameter.

**Headers:**
- gitLabServerURL, e.g., https://gitlab.dev.smartclide.eu
- gitlabToken: <GITLAB_PAT>
- Authorization:  Bearer <BEARER_TOKEN>

