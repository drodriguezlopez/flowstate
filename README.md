# Flowstate Project

## Overview
Flowstate is a modular system designed to manage commands and queries in a scalable, containerized environment. It leverages domain-driven design and supports robust deployment strategies for cloud-native applications.

## Architecture & Modules
- **flowstate-command-api**: Exposes command APIs. [See folder](./flowstate-command-api)
- **flowstate-command-handler**: Handles command processing. [See folder](./flowstate-command-handler)
- **flowstate-query-handler**: Handles query processing. [See folder](./flowstate-query-handler)
- **contracts**: OpenAPI specifications for commands and queries. [See contracts](./contracts)

## Getting Started
### Prerequisites
- Java 21+
- Maven
- Kubernetes (for deployment)

### Setup & Build
```sh
mvn clean install
```

### Run
Refer to each module's README for specific run instructions.

## Usage
- Command API: See [openapi-commands.yaml](./contracts/openapi-commands.yaml)
- Query API: See [openapi-queries.yaml](./contracts/openapi-queries.yaml)

## Domain Context
- Business logic and requirements: [Functional documentation](./doc/functional/README.md)
- Technical diagrams: [Diagrams](./doc/technical/diagrams)

## Development
- Code structure: See submodule READMEs
- Testing: Use Maven test lifecycle
- Contributing: Guidelines TBD
- Infrastructure setup: [Infra docs](./doc/infra)

## Deployment
- Containerization: See Docker/Jib setup in each module
- Kubernetes manifests: [k8s/](./k8s)
- CI/CD pipeline: [Jenkins pipeline](./doc/infra/jenkins/pipeline.groovy)

## Further Resources
- Documentation: See [doc/](./doc)
- Contact/Support: Add contact info here
