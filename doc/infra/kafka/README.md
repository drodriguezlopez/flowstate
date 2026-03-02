# Kafka & Kafka-UI Deployment Guide

> ← Back to [Infrastructure Documentation](../README.md)

## Overview
This directory contains Kubernetes manifests for deploying Apache Kafka and Kafka-UI in the `kafka` namespace. Kafka provides distributed event streaming, while Kafka-UI offers a web interface for managing and monitoring Kafka clusters.

## Prerequisites
- A running Kubernetes cluster (e.g., k0s, k3s, minikube)
- `kubectl` configured to access your cluster
- The `kafka` namespace created (the manifests will create it if not present)
- (Recommended) An Ingress controller installed (see [k0s/README.md](../k0s/README.md) for NGINX Ingress setup)

## Step 1: Deploy Kafka
Apply the Kafka deployment and service manifest:

```bash
kubectl apply -f kafka-deployment.yaml
```

This will create:
- A `Deployment` running Apache Kafka (`apache/kafka:4.2.0`) with controller and broker roles
- A `Service` exposing ports `9092` (broker) and `9093` (controller) as `kafka-service` in the `kafka` namespace

### Verify Kafka
```bash
kubectl get pods -n kafka
kubectl get svc -n kafka
```

## Step 2: Deploy Kafka-UI
Apply the Kafka-UI deployment, service, and ingress manifest:

```bash
kubectl apply -f kafka-ui-dployment.yaml
```

This will create:
- A `Deployment` for Kafka-UI (`provectuslabs/kafka-ui`)
- A `Service` exposing port `8080` as `kafka-ui-service`
- An `Ingress` resource routing traffic from `kafka-ui.rodriguezrodero.com` to the UI

### Access Kafka-UI
- Ensure your Ingress controller is running and configured.
- Add an entry to your `/etc/hosts` (or DNS) pointing `kafka-ui.rodriguezrodero.com` to your cluster/node IP:
  ```
  192.168.1.100 kafka-ui.rodriguezrodero.com
  ```
- Open `http://kafka-ui.rodriguezrodero.com` in your browser.

## Configuration Notes
- Kafka-UI is configured to connect to the Kafka broker via the internal service DNS (`kafka-service.kafka.svc.cluster.local:9092`).
- Dynamic configuration is enabled for Kafka-UI (`DYNAMIC_CONFIG_ENABLED=true`).
- If you change service or ingress hostnames, update the manifests accordingly.

## Troubleshooting
- Check pod logs for errors:
  ```bash
  kubectl logs <pod-name> -n kafka
  ```
- Ensure all services and ingress resources are created and healthy:
  ```bash
  kubectl get all -n kafka
  kubectl describe ingress kafka-ui -n kafka
  ```
- For Ingress issues, verify your controller is running and listening on the expected ports.

## References
- [Apache Kafka](https://kafka.apache.org/)
- [Kafka-UI](https://github.com/provectus/kafka-ui)
- [NGINX Ingress Controller](https://kubernetes.github.io/ingress-nginx/)
- [k0s Kubernetes](https://k0sproject.io/)
