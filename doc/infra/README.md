# Infrastructure Documentation

This documentation provides a comprehensive, expanded review of all files in the `infra` directory and its subdirectories. Each section explains the technical purpose, configuration, and usage of the files, with context for deployment and integration.

---
## k0s/install.md

**Purpose:**
Instructions for installing and configuring k0s, a lightweight Kubernetes distribution.

**Key Details:**
- **Installation:**
  - Download and install k0s using a shell script.
  - Set up a single controller node and start the service.
  - Verify node status with `kubectl`.
- **Ingress Setup:**
  - Deploy NGINX ingress controller for managing external access to services.
  - Annotate ingress class as default.
- **Reference:**
  - Official k0s documentation: https://k0sproject.io/

**Usage:**
- Use these instructions to quickly bootstrap a k0s Kubernetes cluster and enable ingress for your applications.

---
## container-registry/container-registry.yaml

**Purpose:**
Defines a Kubernetes Deployment and Service for a Docker Registry, allowing you to host your own container images within your infrastructure.

**Key Details:**
- **Deployment:**
  - Runs a single replica of the official `registry:2` Docker image.
  - Uses a persistent volume (`/mnt/data/registry`) on the host to store images, ensuring data is retained across pod restarts.
  - Namespace: `devops-tools`.
- **Service:**
  - Exposes the registry on port 5000 via a NodePort (30500), making it accessible within and outside the cluster.

**Usage:**
- Deploy this YAML to your Kubernetes cluster to set up a private Docker registry.
- Images pushed to this registry are stored persistently on the node.
- Useful for CI/CD pipelines and local development.

---

## jenkins/jenkins.yaml

**Purpose:**
Comprehensive Kubernetes configuration for deploying Jenkins in a secure, persistent, and scalable manner.

**Key Details:**
- **Namespace:** Creates `devops-tools` for isolation.
- **RBAC:**
  - Defines a `ClusterRole` and `ClusterRoleBinding` for Jenkins admin access.
  - ServiceAccount `jenkins-admin` is used for pod operations.
- **Storage:**
  - PersistentVolume and PersistentVolumeClaim for Jenkins data (`/mnt/data/jenkins`, 8Gi).
- **Deployment:**
  - Jenkins runs as a single pod with resource limits (2Gi memory, 1000m CPU).
  - Uses the official `jenkins/jenkins:lts` image.
  - Security context set for filesystem permissions.
  - Exposes HTTP (8080) and JNLP (50000) ports.

**Usage:**
- Apply this YAML to deploy Jenkins with persistent storage and proper RBAC.
- Integrate with Kubernetes for dynamic agent provisioning.

---

## jenkins/pipeline.groovy

**Purpose:**
Defines a Jenkins pipeline for building and deploying Java applications using Maven inside Kubernetes pods.

**Key Details:**
- **Agent:**
  - Uses a Kubernetes pod with a Maven container (`maven:3.9.6-eclipse-temurin-21`).
- **Stages:**
  - **Checkout:** Clones the repository from GitHub (branch: develop).
  - **Build:** Runs `mvn clean package` (skipping tests for speed).
  - **Deploy:** Uses Jib Maven plugin to build and push container images.
- **Post:**
  - On success, echoes a completion message.

**Usage:**
- Place this pipeline in your Jenkins project to automate build and deployment in a Kubernetes-native way.
- Ensures builds are isolated and reproducible.

---

## jenkins/README.md

**Purpose:**
Step-by-step guide for integrating Jenkins with Kubernetes, enabling dynamic agent provisioning and scalable CI/CD.

**Key Details:**
- **Prerequisites:** Kubernetes cluster, Jenkins installation, admin access.
- **Plugin Installation:** Instructions for installing the Kubernetes plugin in Jenkins.
- **Cloud Configuration:** How to connect Jenkins to your Kubernetes cluster, including namespace and credentials setup.
- **Pod Templates:** Defines how Jenkins agents are created as pods, with labels and container images.
- **Pipeline Example:** Shows a Jenkinsfile for running jobs on Kubernetes agents.
- **Security (RBAC):** Provides YAML for necessary permissions to allow Jenkins to create pods.

**Usage:**
- Follow this guide to set up Jenkins with Kubernetes for scalable CI/CD.
- Use the provided pipeline and RBAC examples for quick integration.

---