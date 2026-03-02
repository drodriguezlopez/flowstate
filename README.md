# Flowstate Project

## Overview
Flowstate is a modular system designed to manage commands and queries in a scalable, containerized environment. It leverages domain-driven design and supports robust deployment strategies for cloud-native applications.

## Architecture & Modules
- **flowstate-command-handler**: Handles command processing. [See folder](./flowstate-command-handler)
- **flowstate-query-handler**: Handles query processing. [See folder](./flowstate-query-handler)
- **contracts**: OpenAPI specifications for commands and queries. [See contracts](./contracts)

## Getting Started
### Prerequisites
- Java 21+
- Maven
- Docker registry accessible at the URL you set via `REGISTRY_URL` (see below)
- Kubernetes (for deployment)

### Environment Variables
| Variable | Description | Example |
|---|---|---|
| `REGISTRY_URL` | Hostname (and optional path prefix) of the container registry used by the Jib Maven plugin to push images. | `registry.example.com` |

### Setup & Build
```sh
mvn clean install
```

### Run

#### Command Handler (port 8080)
```sh
cd flowstate-command-handler
mvn spring-boot:run
```
Requires a running MySQL instance. Configure the connection in `src/main/resources/application.yaml` or override via environment variables:
```sh
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/flowstate
SPRING_DATASOURCE_USERNAME=flowuser
SPRING_DATASOURCE_PASSWORD=password
```

#### Query Handler (port 8081)
```sh
cd flowstate-query-handler
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
Uses an in-memory H2 database when running with the `local` profile. The H2 console is available at `http://localhost:8081/h2-console`.

## Usage
- Command API: See [openapi-commands.yaml](./contracts/openapi-commands.yaml)
- Query API: See [openapi-queries.yaml](./contracts/openapi-queries.yaml)

## Domain Context
- Business logic and requirements: [Functional documentation](./doc/functional/README.md)
- Technical diagrams:
  - [Outbox Pattern Architecture](./doc/technical/diagrams/arquitectura-outbox.puml)
  - [k0s Deployment (High Level)](./doc/technical/diagrams/k0s-deployment-highlevel.puml)
  - [k0s Deployment (Detailed)](./doc/technical/diagrams/k0s-deployment.puml)

## Development
- Code structure: See submodule READMEs
- Testing: Use Maven test lifecycle
- Contributing: Guidelines TBD
- Infrastructure setup: [Infra docs](./doc/infra/README.md)

## Deployment
- Containerization: Images are built and pushed with [Jib](https://github.com/GoogleContainerTools/jib). Set `REGISTRY_URL` to your registry hostname before running:
  ```sh
  export REGISTRY_URL=registry.example.com   # Linux/macOS
  set REGISTRY_URL=registry.example.com      # Windows CMD
  $env:REGISTRY_URL="registry.example.com"  # Windows PowerShell

  mvn compile jib:build
  ```
  The resulting image is tagged as `$REGISTRY_URL/<module>:latest`.
  > ⚠️ The Jib plugin is configured with `allowInsecureRegistries: true`. Use a registry secured with TLS in production and remove that flag.
- Kubernetes manifests: [k8s/](./k8s)
- CI/CD pipeline: [Jenkins pipeline](./doc/infra/jenkins/pipeline.groovy)

## Further Resources
- Documentation: See [doc/README.md](./doc/README.md)
- Contact/Support: Add contact info here
