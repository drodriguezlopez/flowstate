# Infrastructure Documentation

> ← Back to [Documentation Index](../README.md)

This folder contains setup guides and Kubernetes manifests for every layer of the FlowState platform.

---

## Components

| Component | Document / File | Description |
|---|---|---|
| k0s Cluster | [k0s/README.md](./k0s/README.md) | Install k0s, bootstrap a single-node cluster, and configure the NGINX ingress controller |
| NGINX Ingress Manifest | [k0s/nginx ingress/deploy.yaml](./k0s/nginx%20ingress/deploy.yaml) | NGINX ingress controller Kubernetes manifest with `hostNetwork: true` |
| Container Registry | [container-registry/container-registry.yaml](./container-registry/container-registry.yaml) | Kubernetes Deployment + NodePort Service for a self-hosted `registry:2` in `devops-tools` |
| Nginx Host / SSL | [nginx/README.md](./nginx/README.md) | Wildcard SSL certificate via Certbot + Cloudflare DNS plugin on the Nginx reverse proxy host |
| Jenkins Setup | [jenkins/README.md](./jenkins/README.md) | Step-by-step guide to connect Jenkins to the k0s cluster and configure dynamic pod agents |
| Jenkins Manifest | [jenkins/jenkins.yaml](./jenkins/jenkins.yaml) | Full Jenkins deployment: RBAC, PersistentVolume, resource limits, HTTP and JNLP ports |
| Jenkins Pipeline | [jenkins/pipeline.groovy](./jenkins/pipeline.groovy) | Groovy pipeline: checkout → `mvn clean package` → `jib:build` push to private registry |
