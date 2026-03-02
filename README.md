# Flowstate Project

## Overview
FlowState is a cloud-native task management system built on a microservices architecture. Each service is independently deployable and responsible for a distinct bounded context within the task management domain. The system applies Command Query Responsibility Segregation (CQRS) to separate write and read concerns, enabling scalability and resilience across all components.

## Architecture & Modules
- **[flowstate-command-handler](./flowstate-command-handler/README.md)**: Handles command processing (Write side, port 8080). Requires a running MySQL instance.
- **[flowstate-query-handler](./flowstate-query-handler/README.md)**: Handles query processing (Read side, port 8081). Uses an in-memory H2 database in the `local` profile.
- **[contracts](./contracts)**: OpenAPI specifications — [commands](./contracts/openapi-commands.yaml) · [queries](./contracts/openapi-queries.yaml)

## Getting Started
### Prerequisites
- Java 21+
- Maven
- Docker registry (set `REGISTRY_URL` — see [Deployment](#deployment))
- Kubernetes (for deployment)

### Build
```sh
mvn clean install
```

### Run
See each module's README for detailed run instructions and configuration:
- [Command Handler](./flowstate-command-handler/README.md)
- [Query Handler](./flowstate-query-handler/README.md)

## Deployment
Images are built and pushed with [Jib](https://github.com/GoogleContainerTools/jib):
```sh
# Set your registry hostname first
$env:REGISTRY_URL="registry.example.com"   # PowerShell
# export REGISTRY_URL=registry.example.com  # Linux/macOS

mvn compile jib:build
```
> ⚠️ The Jib plugin is configured with `allowInsecureRegistries: true`. Use a TLS-secured registry in production and remove that flag.

- Kubernetes manifests: [k8s/](./k8s)
- CI/CD pipeline & infrastructure setup: [doc/infra/README.md](./doc/infra/README.md)

## Documentation
Full documentation index: **[doc/README.md](./doc/README.md)**

| Area | Link |
|---|---|
| Functional / Domain | [doc/functional/README.md](./doc/functional/README.md) |
| Technical / Architecture | [doc/technical/README.md](./doc/technical/README.md) |
| Infrastructure & CI/CD | [doc/infra/README.md](./doc/infra/README.md) |
