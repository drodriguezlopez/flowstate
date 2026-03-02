# FlowState – Documentation Index

> ← Back to [project README](../README.md)

This folder contains all documentation for the FlowState project, organised into three areas: functional domain knowledge, technical architecture, and infrastructure setup.

---

## Structure

```
doc/
├── functional/          # Domain model and business rules
├── technical/           # Architecture, CQRS flow, and diagrams
└── infra/               # Infrastructure setup and CI/CD
```

---

## Functional

Business domain concepts, entities, and rules that drive the system.

| Document | Description |
|---|---|
| [Domain Overview](./functional/README.md) | Task aggregate root, read models (TaskSummary, TaskHistory), and CQRS responsibilities |
| [Domain Model Diagram](./functional/domain.puml) | PlantUML class diagram of Task, TaskSummary, TaskHistory, Status, and Priority |

---

## Technical

Architecture decisions, module interactions, and deployment topology.

| Document | Description |
|---|---|
| [Architecture Overview](./technical/README.md) | CQRS structure, Outbox Pattern flow, module breakdown, and technology stack |
| [Outbox Architecture Diagram](./technical/diagrams/arquitectura-outbox.puml) | Module-level data flow from command submission through event relay to read store |
| [k0s Deployment – High Level](./technical/diagrams/k0s-deployment-highlevel.puml) | High-level cluster topology: Nginx reverse proxy, ingress, devops-tools, and flowstate namespaces |
| [k0s Deployment – Detailed](./technical/diagrams/k0s-deployment.puml) | Full per-namespace component breakdown including services, config maps, volumes, and ingress rules |

---

## Infrastructure

Setup guides and Kubernetes manifests for every layer of the platform.

| Document | Description |
|---|---|
| [Infrastructure Overview](./infra/README.md) | Summary of all infra components: registry, Jenkins, k0s, Nginx |
| [k0s Installation Guide](./infra/k0s/README.md) | Install k0s, bootstrap a single-node cluster, and configure the NGINX ingress controller |
| [k0s Nginx Ingress Manifest](infra/k0s/nginx ingress/deploy.yaml) | NGINX ingress controller Kubernetes manifest with `hostNetwork: true` |
| [Container Registry](./infra/container-registry/container-registry.yaml) | Kubernetes Deployment + NodePort Service for a self-hosted `registry:2` in `devops-tools` |
| [Nginx Host / SSL Setup](infra/nginx/README.md) | Wildcard SSL certificate via Certbot + Cloudflare DNS plugin on the Nginx reverse proxy host (`haproxy/` folder) |
| [Jenkins on Kubernetes](./infra/jenkins/README.md) | Step-by-step guide to connect Jenkins to the k0s cluster and configure dynamic pod agents |
| [Jenkins Kubernetes Manifest](./infra/jenkins/jenkins.yaml) | Full Jenkins deployment: RBAC, PersistentVolume, resource limits, HTTP and JNLP ports |
| [Jenkins Pipeline](./infra/jenkins/pipeline.groovy) | Groovy pipeline: checkout → `mvn clean package` → `jib:build` push to private registry |

