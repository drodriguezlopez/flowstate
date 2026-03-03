# Kafka & Kafka-UI Deployment Guide

> ← Back to [Infrastructure Documentation](../README.md)

## Overview
This directory contains Kubernetes manifests for deploying Apache Kafka, Kafka-UI, Debezium, and MySQL (for CDC) in the `kafka` namespace. Kafka provides distributed event streaming, Kafka-UI offers a web interface for managing and monitoring Kafka clusters, and Debezium enables Change Data Capture (CDC) from MySQL to Kafka topics.

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

## Step 3: Deploy Debezium Kafka Connect
Apply the Debezium Connect deployment, service, and ingress manifest:

```bash
kubectl apply -f debezium-connect-deployment.yaml
```

This will create:
- A `Deployment` for Debezium Kafka Connect (`quay.io/debezium/connect:3.4.1.Final`, compatible with Kafka 4.x KRaft)
- A `Service` exposing the Kafka Connect REST API on port 8083 as `debezium-connect-service`
- An `Ingress` resource routing traffic from `cdc.rodriguezrodero.com` to the Connect REST API

## Step 4: Configure Debezium MySQL Connector

The connector config is in `mysql-connector-config.json`. Key fields (Debezium 3.x):

| Field | Value | Notes |
|---|---|---|
| `database.hostname` | `mysql-service.flowstate` | Short-form DNS — MySQL is in a different namespace (`flowstate`) |
| `topic.prefix` | `dbserver1` | Replaces deprecated `database.server.name` (Debezium 2.0+) |
| `schema.history.internal.kafka.bootstrap.servers` | `kafka-service.kafka:9092` | Replaces deprecated `database.history.kafka.bootstrap.servers` (Debezium 2.0+) |
| `schema.history.internal.kafka.topic` | `dbhistory.flowstate` | Internal topic for schema change history |

1. Edit `mysql-connector-config.json` if needed (database, table names, credentials, etc).
2. Port-forward the Debezium Connect service:

```bash
kubectl port-forward svc/debezium-connect-service 8083:8083 -n kafka
```

3. Register the connector:

```bash
curl -X POST -H "Content-Type: application/json" \
  --data @mysql-connector-config.json \
  http://localhost:8083/connectors
```

### Verify the Connector

Check the connector was registered and is running:

```bash
# List all registered connectors
curl http://localhost:8083/connectors

# Check connector status (should show state: RUNNING)
curl http://localhost:8083/connectors/mysql-connector/status
```

Expected healthy output:
```json
{
  "name": "mysql-connector",
  "connector": { "state": "RUNNING", ... },
  "tasks": [{ "id": 0, "state": "RUNNING", ... }]
}
```

If the task shows `FAILED`, check the connector error:
```bash
curl http://localhost:8083/connectors/mysql-connector/status | jq '.tasks[0].trace'
```

## Step 5: Verify CDC Events
- Insert or update data in MySQL (`mysql-service`).
- Consume change events from Kafka topics named like `dbserver1.flowstate.<table>` using your preferred Kafka consumer or Kafka-UI.

## Configuration Notes
- Kafka-UI is configured to connect to the Kafka broker via the internal service DNS (`kafka-service.kafka.svc.cluster.local:9092`).
- Debezium Connect is configured to use the same Kafka broker and will stream MySQL changes to Kafka topics.
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
- For Debezium issues, check the logs of the Debezium Connect pod and ensure MySQL binlog is enabled and accessible.

### `POST /connectors` returns HTTP 500 after ~90 s

Two root causes have been observed:

#### 1. `KAFKA_ADVERTISED_LISTENERS` used a short hostname
The broker was advertising itself as `kafka-service:9092`. Clients that bootstrapped with the
full FQDN (`kafka-service.kafka.svc.cluster.local:9092`) would receive the short name back
and might fail to resolve it from different namespaces.

**Fix:** `KAFKA_ADVERTISED_LISTENERS` is now set to `PLAINTEXT://kafka-service.kafka.svc.cluster.local:9092`.

#### 2. Missing replication privileges for `flowuser`
Debezium CDC requires **global-level** privileges (`REPLICATION CLIENT`, `REPLICATION SLAVE`)
that are **not** included in a database-scoped `ALL PRIVILEGES ON flowstate.*` grant.
Without them, the connector task fails immediately after being registered.

**Fix:** `mysql-deployment.yaml` (`init-grant.sql`) now also grants:
```sql
GRANT REPLICATION CLIENT ON *.* TO 'flowuser'@'%';
GRANT REPLICATION SLAVE  ON *.* TO 'flowuser'@'%';
GRANT SELECT ON performance_schema.* TO 'flowuser'@'%';
```
If MySQL is already running, apply the grants manually:
```bash
kubectl exec -it <mysql-pod> -n flowstate -- \
  mysql -uroot -prootpass -e "
    GRANT REPLICATION CLIENT ON *.* TO 'flowuser'@'%';
    GRANT REPLICATION SLAVE  ON *.* TO 'flowuser'@'%';
    GRANT SELECT ON performance_schema.* TO 'flowuser'@'%';
    FLUSH PRIVILEGES;"
```

## References
- [Apache Kafka](https://kafka.apache.org/)
- [Kafka-UI](https://github.com/provectus/kafka-ui)
- [Debezium](https://debezium.io/)
- [Debezium MySQL Connector Docs](https://debezium.io/documentation/reference/stable/connectors/mysql.html)
- [NGINX Ingress Controller](https://kubernetes.github.io/ingress-nginx/)
- [k0s Kubernetes](https://k0sproject.io/)
