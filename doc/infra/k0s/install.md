# k0s Kubernetes Installation Guide

## Overview
k0s is a lightweight, certified Kubernetes distribution designed for simplicity and flexibility. It runs on any infrastructure—bare-metal, on-premises, edge, IoT, public and private clouds—and is 100% open source. k0s is ideal for both development and production environments due to its minimal setup and robust features.

## Prerequisites
- A Linux server or VM with root access
- Internet connectivity for downloading packages
- Basic familiarity with shell commands

## Step 1: Install k0s
The following commands will download and install k0s, set up a single-node controller, and start the cluster.

```bash
curl -sSf https://get.k0s.sh | sudo sh
sudo k0s install controller --single
sudo k0s start   # Wait about a minute for the cluster to initialize
```

### Verification
Check that your node is running and ready:
```bash
sudo k0s kubectl get nodes
```
You should see your node listed as Ready.

## Step 2: Set Up Ingress Controller
Ingress controllers manage external access to services in your cluster. The following commands deploy the NGINX ingress controller and set it as the default ingress class.

```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.1.3/deploy/static/provider/baremetal/deploy.yaml
kubectl -n ingress-nginx annotate ingressclasses nginx ingressclass.kubernetes.io/is-default-class="true"
```

### Notes
- Ensure your cluster has network connectivity to download the manifest.
- The annotation sets NGINX as the default ingress class for new ingress resources.

## Troubleshooting
- If you encounter issues with installation, check the k0s logs:
  ```bash
  sudo k0s status
  sudo k0s kubectl get pods -A
  ```
- For networking issues, verify firewall rules and that required ports are open.
- For persistent problems, consult the official documentation or community forums.

## References & Further Reading
- Official k0s documentation: https://k0sproject.io/
- Kubernetes Ingress documentation: https://kubernetes.io/docs/concepts/services-networking/ingress/
- NGINX Ingress Controller: https://kubernetes.github.io/ingress-nginx/
